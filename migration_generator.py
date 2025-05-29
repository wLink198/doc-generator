import tiktoken
from google import genai

def count_tokens(text: str) -> int:
    encoder = tiktoken.get_encoding("cl100k_base")
    return len(encoder.encode(text))

def create_migration_prompt(existing_doc: str) -> str:
    return f"""
Bạn là một chuyên gia Cloud Architect.

Dưới đây là tài liệu mô tả hệ thống phần mềm hiện tại, đang vận hành on-premise.

--- BẮT ĐẦU MÔ TẢ HIỆN TRẠNG ---
{existing_doc}
--- KẾT THÚC MÔ TẢ ---

## Nhiệm vụ của bạn:
1. Phân tích hệ thống hiện tại và xác định rõ các thành phần hạ tầng.
2. Đề xuất một kiến trúc tương đương nhưng tối ưu trên cloud (có thể dùng AWS, GCP hoặc Azure), thay vì gen ra biểu đồ uml hãy gen ra code python để chạy đoạn code đó sẽ gen ra ảnh với định dạng png hoặc jpg.
3. Xây dựng kế hoạch migration gồm các bước cụ thể.
4. Liệt kê các rủi ro tiềm ẩn và cách giảm thiểu.
5. Đưa ra các khuyến nghị về bảo mật, logging, scaling, monitoring.

### Yêu cầu trình bày:
- Dùng định dạng Markdown với các tiêu đề rõ ràng
- Trình bày gọn gàng, tập trung vào kiến trúc hệ thống, giọng văn chuyên nghiệp
- Không phân tích nghiệp vụ hay logic tầng controller/service
- **Trả lời bằng tiếng Việt**
###

### Trình bày bằng tiếng Việt, rõ ràng, định dạng Markdown.
"""

def call_gemini(prompt: str, api_key: str) -> str:
    try:
        client = genai.Client(api_key=api_key)
        response = client.models.generate_content(
            model="gemini-2.0-flash",
            contents=prompt
        )
        return response.text
    except Exception as e:
        print(f"Lỗi khi gọi Gemini: {e}")
        return ""

def main():
    api_key = input("🔑 Nhập Gemini API key: ")

    with open("system_documentation.md", "r", encoding="utf-8") as f:
        input_text = f.read()

    print(f"📊 Tổng số tokens: {count_tokens(input_text)}")

    prompt = create_migration_prompt(input_text)
    result = call_gemini(prompt, api_key)

    if result:
        with open("migration_to_cloud.md", "w", encoding="utf-8") as f:
            f.write(result)
        print("✅ Đã lưu vào 'migration_to_cloud.md'")
    else:
        print("❌ Không thể tạo tài liệu migration.")

if __name__ == "__main__":
    main()
