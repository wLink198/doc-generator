import os
import glob
from typing import List
from google import genai

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

def read_and_minimize_all_java(folder_path: str) -> str:
    files = get_java_files(folder_path)
    all_code = ""
    for file_path in files:
        content = read_file_content(file_path)
        if content:
            minimized = minimize_java_code(content)
            all_code += f"// File: {os.path.basename(file_path)}\n{minimized}\n\n"
    return all_code

def create_business_doc_prompt(all_code: str) -> str:
    return f"""
Bạn là một chuyên gia phân tích phần mềm với kiến thức sâu sắc về ứng dụng Java và nghiệp vụ.

Dưới đây là toàn bộ mã nguồn Java của dự án.

Nhiệm vụ của bạn là phân tích mã và tạo ra một tài liệu **nghiệp vụ** chi tiết giải thích:

- Lĩnh vực nghiệp vụ và các chức năng cốt lõi được triển khai
- Các thực thể chính và vai trò của chúng trong ngữ cảnh nghiệp vụ
- Cách các thành phần tương tác để hoàn thành các quy trình nghiệp vụ
- Ví dụ về các kịch bản sử dụng hoặc luồng công việc điển hình
- Các quy tắc hoặc ràng buộc nghiệp vụ quan trọng được nhúng trong mã

Tập trung giải thích ở góc độ nghiệp vụ, tránh các chi tiết kỹ thuật thấp.

Trả về tài liệu theo định dạng Markdown với các phần và tiêu đề rõ ràng.

Mã nguồn Java:

```java
{all_code}
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

def generate_business_doc(folder_path: str, api_key: str) -> str:
    all_code = read_and_minimize_all_java(folder_path)
    if not all_code:
        print("No Java code found or failed to read files.")
        return ""
    prompt = create_business_doc_prompt(all_code)
    return call_gemini_api(prompt, api_key)

def main():
    folder_path = input("Enter folder path containing Java files: ")
    api_key = input("Enter your Gemini API key: ")
    business_doc = generate_business_doc(folder_path, api_key)
    if business_doc:
        output_file = "business_documentation.md"
        with open(output_file, 'w', encoding='utf-8') as f:
            f.write(business_doc)
        print(f"Business documentation generated and saved to {output_file}")
    else:
        print("Failed to generate business documentation.")

if __name__ == "__main__":
    main()
