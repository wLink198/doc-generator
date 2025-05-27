import os
from collections import defaultdict
from typing import List
from google import genai
import tiktoken

def count_tokens(text: str) -> int:
    # Dùng encoder gần giống với Gemini
    encoder = tiktoken.get_encoding("cl100k_base")  # Tạm dùng encoder của GPT-4
    return len(encoder.encode(text))

def is_irrelevant_path(path: str) -> bool:
    path = path.lower()
    skip_keywords = [
        "test", "dto", "entity", "model", "sample", "mock", 
        "mapper", "convert", "vo", "constants", "util", "helper", "config"
    ]
    return any(k in path for k in skip_keywords)

def is_irrelevant_file(filename: str) -> bool:
    filename = filename.lower()
    skip_suffixes = [
        "test.java", "dto.java", "entity.java", "vo.java", 
        "mapper.java", "util.java", "helper.java", "config.java", "constant.java"
    ]
    return any(filename.endswith(suffix) for suffix in skip_suffixes)

def get_relevant_files(folder_path: str) -> list:
    """Scan source folder and filter only meaningful infra-related files."""
    relevant_files = []

    for root, dirs, files in os.walk(folder_path):
        rel_path = os.path.relpath(root, folder_path)
        if is_irrelevant_path(rel_path):
            continue
        for file in files:
            if not file.endswith(".java"):
                continue
            if is_irrelevant_file(file):
                continue
            full_path = os.path.join(root, file)
            relevant_files.append(full_path)

    # Include config/build files even if in root
    for config_file in ['pom.xml', 'build.gradle', 'application.yml', 'application.yaml', 'application.properties']:
        config_path = os.path.join(folder_path, config_file)
        if os.path.exists(config_path):
            relevant_files.append(config_path)
    return relevant_files

def get_java_files(folder_path: str) -> List[str]:
    """Get all .java files in folder and subfolders."""
    if not os.path.exists(folder_path):
        print(f"Folder {folder_path} does not exist")
        return []
    return glob.glob(os.path.join(folder_path, "**/*.java"), recursive=True)

def read_file_content(file_path: str) -> str:
    """Read file content, return empty string if error."""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            return f.read()
    except Exception as e:
        print(f"Error reading {file_path}: {e}")
        return ""

def minimize_java_code(java_code: str) -> str:
    """Minimize java code by removing comments and excess whitespace."""
    import re
    # Remove block comments /* ... */
    code = re.sub(r'/\*.*?\*/', '', java_code, flags=re.DOTALL)
    # Remove line comments //
    code = re.sub(r'//.*', '', code)
    # Remove excess empty lines and leading/trailing spaces
    code = '\n'.join(line.strip() for line in code.splitlines() if line.strip())
    return code

def split_code_by_type(folder_path: str) -> dict:
    result = defaultdict(str)
    relevant_files = get_relevant_files(folder_path)

    for file_path in relevant_files:
        content = read_file_content(file_path)
        if not content:
            continue

        fname = os.path.basename(file_path)

        if fname.endswith(".java"):
            minimized = minimize_java_code(content)
            result["java_code"] += f"// File: {fname}\n{minimized}\n\n"
        elif fname in ["pom.xml", "build.gradle"]:
            result["build_config"] += f"# File: {fname}\n{content}\n\n"
        elif fname.startswith("application.") and fname.endswith((".yml", ".yaml", ".properties")):
            result["app_config"] += f"# File: {fname}\n{content}\n\n"
    return result

def create_sys_doc_prompt(java_code: str, build_config: str, app_config: str) -> str:
    return f"""
Bạn là một kiến trúc sư hệ thống có kinh nghiệm triển khai ứng dụng từ môi trường cục bộ (local) lên cloud (như GCP, AWS, Azure...).
Dưới đây là mã nguồn Java và cấu hình hệ thống phần mềm đang chạy local.
## Mục tiêu: Phân tích toàn bộ hệ thống **về mặt kiến trúc hạ tầng**, để phục vụ cho việc **chuyển đổi từ local lên cloud**.
## Nhiệm vụ của bạn:
1. **Xác định các thành phần hạ tầng chính** trong hệ thống, bao gồm nhưng không giới hạn ở:
   - Web/API backend (vd: Spring Boot)
   - Database (vd: MySQL, PostgreSQL, MongoDB)
   - Caching (vd: Redis, Memcached)
   - Message broker (vd: Kafka, RabbitMQ)
   - External API integrations (vd: OAuth2, Email, Payment Gateway)
   - Storage/file handling (vd: lưu file cục bộ, cloud storage)
   - Scheduled Jobs/Batch Processing
   - Config management (application.properties, YAML, env)
   - Deployment method (Docker, local server, etc.)
   - Any cloud-specific integration (nếu có)
2. **Mô tả chi tiết cách các thành phần trên giao tiếp hoặc phụ thuộc lẫn nhau**
3. **Tóm tắt các thư viện, framework chính ảnh hưởng đến kiến trúc hạ tầng** (Spring Boot, Hibernate, Kafka client, etc.)
4. **Xác định các ràng buộc deployment hoặc dependency bên ngoài cần xử lý khi migrate** (vd: hard-coded path, file upload, local storage, etc.)
### Yêu cầu trình bày:
- Dùng định dạng Markdown với các tiêu đề rõ ràng
- Trình bày gọn gàng, tập trung vào kiến trúc hệ thống, giọng văn chuyên nghiệp
- Không phân tích nghiệp vụ hay logic tầng controller/service
- **Trả lời bằng tiếng Việt**
### Mã nguồn Java:
```java
{java_code}
```

### Cấu hình build (pom.xml hoặc gradle):
```text
{build_config}
```

### Cấu hình ứng dụng:
```text
{app_config}
```
"""

def call_gemini_api(prompt: str, api_key: str) -> str:
    try:
        client = genai.Client(api_key=api_key)
        response = client.models.generate_content(
            model="gemini-2.0-flash",
            contents=prompt
        )
        return response.text
    except Exception as e:
        print(f"Error calling Gemini API: {e}")
        return ""

def generate_system_doc(folder_path: str, api_key: str) -> str:
    code_parts = split_code_by_type(folder_path)
    full_code = code_parts["java_code"] + code_parts["build_config"] + code_parts["app_config"]
    total_tokens = count_tokens(full_code)
    print(f"Number of tokens: {total_tokens}")
    prompt = create_sys_doc_prompt(
        java_code=code_parts["java_code"],
        build_config=code_parts["build_config"],
        app_config=code_parts["app_config"]
    )
    return call_gemini_api(prompt, api_key)

def main():
    folder_path = input("Enter folder path: ")
    api_key = input("Enter Gemini API key: ")
    result = generate_system_doc(folder_path, api_key)
    if result:
        with open("system_documentation.md", "w", encoding="utf-8") as f:
            f.write(result)
        print("Saved to system_documentation.md")
    else:
        print("Failed to generate documentation.")

if __name__ == "__main__":
    main()
