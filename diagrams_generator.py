import doc_generator
from pathlib import Path

# --------- Prompt để yêu cầu Gemini sinh sơ đồ hệ thống bằng diagrams ---------

IMPORT_LIST = doc_generator.read_file_content("./diagrams_icons.py")

MARKDOWN_DOC = doc_generator.read_file_content("./system_documentation.md")

LAYER_ICON = "./layer.png"

prompt_text = f"""
You are generating Python code using the diagrams library to represent system architecture described in a markdown (.md) file.

🧩 Your goals:
- Extract only clear and explicitly described components from the input (do NOT assume or hallucinate anything).
- Generate only diagrams for components with full and valid information (e.g., type and role). Ignore vague or underspecified parts.
- Focus only on system architecture and component relations.
- Do not miss any application layer
- Do not miss any external component

🛑 Strict constraints:
- You must only use the following list of node classes and their aliases for imports and component nodes. Do not use anything else.

{IMPORT_LIST}

🟦 For application layers like Controller, Service, Repository or other conceptual layers not tied to specific technologies, use:
from diagrams.custom import Custom
and represent them as rectangular nodes with the layer name and link to the icon: {LAYER_ICON} (e.g., Custom("Service", "{LAYER_ICON}")).

📦 You may group related components using Cluster with meaningful group names if evident from the input.

📎 Output must be:
- A valid Python script using the diagrams package.
- Contain only necessary imports from the list above.
- Do not import or reference any unsupported nodes.
- Do not explain the code or add comments.

🎯 Objective: Generate a clean, valid diagram code, strictly based on given components and valid node list. 
Skip anything unclear or not represented by the allowed node classes.
Now, generate the diagram code based on the markdown content below:

\"\"\"{MARKDOWN_DOC}\"\"\"
"""

# --------- Gọi Gemini để lấy mã sơ đồ hệ thống ---------

def generate_arch_diagram_code(api_key: str):
    print("⏳ Gửi yêu cầu tới Gemini...")
    response = doc_generator.call_gemini_api(prompt_text, api_key)

    if not response:
        print("⚠️ Không nhận được phản hồi từ Gemini.")
        return

    # Tùy response của Gemini mà bạn có thể cần xử lý cắt đoạn code:
    # Dưới đây là cách đơn giản để lọc code Python:
    import re
    match = re.search(r"```python\n(.*?)```", response, re.DOTALL)
    code = match.group(1) if match else response

    # Ghi ra file hoặc chạy
    output_file = Path("generated_diagram.py")
    output_file.write_text(code)
    print(f"✅ Code đã được ghi vào {output_file.resolve()}")

    # (Tuỳ chọn) chạy luôn
    run_now = input("Bạn có muốn chạy sơ đồ luôn không? (y/n): ").strip().lower()
    if run_now == 'y':
        print("🏃 Đang chạy sơ đồ...")
        exec(code, globals())
    else:
        print("Bạn có thể chạy bằng: python generated_diagram.py")

# ----------------- Main Entry -----------------

if __name__ == "__main__":
    import os
    generate_arch_diagram_code('AIzaSyAo35HydFtz-auZNlAn6etxCOOPghue848')
