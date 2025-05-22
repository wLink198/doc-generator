# doc-generator

Tổng hợp ý tưởng: Tool gen & update doc tự động cho project code
1️⃣ Đầu vào là gì?
Code project (toàn bộ source hoặc folder)

(Khi update) Code thay đổi / diff (option)

Tài liệu cũ (Markdown, có thể rải rác hoặc tổng hợp)

Vector DB chứa embedding tài liệu cũ (để tìm đoạn tương ứng)

Embedding model (text-embedding-3-small, bge-small...)

AI model (Gemini/OpenAI) để gen & update tài liệu

2️⃣ Đầu ra là gì?
Tài liệu Markdown tổng hợp mô tả project, class, method, workflow

Doc được cập nhật chính xác theo thay đổi code (nếu có)

Biểu đồ workflow/architecture (Mermaid/PlantUML) kèm theo

Changelog tóm tắt phần tài liệu mới hoặc sửa

Log lý do chỉnh sửa từ AI (tuỳ chọn)

3️⃣ Tính thực tế như thế nào?
Gen doc từ code lớn hoàn toàn khả thi với AI hiện tại, nhưng

Hạn chế lớn: Token limit của AI (Gemini ~1M token nhưng gọi 1 lần là kém hiệu quả, còn OpenAI ~4K-32K)

Vector DB giúp chia nhỏ doc thành đoạn embedding, chỉ gọi AI với phần cần thiết → giảm token, tăng hiệu quả

Cần pipeline hợp lý: extract → embed → store → search → update

Tự động update doc khi code thay đổi rất thực tế nếu ta dùng diff + vector DB semantic search → AI không cần đọc hết doc cũ

Workflow diagram có thể tự generate từ doc hoặc code, không tốn nhiều token

4️⃣ Tính khả thi như thế nào?
Thành phần	Khả thi & Chi phí
Extract code	Rất khả thi, free
Vector DB (FAISS/Qdrant local)	Hoàn toàn free, nhanh, chạy local
Embedding models	Nhiều model free
AI model (Gemini API)	Có free tier, quota giới hạn, cần tối ưu gọi API
Workflow diagram (Mermaid/PlantUML)	Free, open source
Tổng thể pipeline	Hoàn toàn có thể build & chạy free 100%, nếu kiểm soát token & gọi AI đúng cách

5️⃣ Lợi ích thu được khi áp dụng ý tưởng
Giảm tối đa thời gian và chi phí tạo tài liệu, so với viết thủ công

Tài liệu luôn cập nhật sát với code, tăng chất lượng maintainability

Tận dụng AI để review, chỉnh sửa chứ không gen lại toàn bộ → giữ phong cách, tránh mất context

Vector DB giúp tối ưu token usage và tìm kiếm semantic trong doc

Tạo được biểu đồ nghiệp vụ, giúp team hiểu nhanh

Pipeline có thể tích hợp CI/CD tự động hóa hoàn toàn

6️⃣ Các tính năng / yêu cầu cơ bản của sản phẩm
Extract toàn bộ code từ project, tạo doc tổng quan ban đầu

Chia doc thành đoạn nhỏ, embed vào vector DB

Tìm kiếm đoạn doc liên quan theo code thay đổi để update chính xác

Tạo tài liệu Markdown hoàn chỉnh, có mô tả, flowchart

Log thay đổi và lý do chỉnh sửa

Tích hợp command line tool dễ dùng, có thể chạy local 100%

Tối ưu token gọi AI, tránh gọi toàn bộ doc nhiều lần

Hỗ trợ đa dạng ngôn ngữ lập trình sau này (tối ưu dần)

7️⃣ Draft cách thực hiện (diagram architecture)
css
Sao chép
Chỉnh sửa
               ┌─────────────┐
               │ Code Project│
               └──────┬──────┘
                      ▼
           ┌─────────────────────┐
           │ Code Extractor       │
           └─────────┬───────────┘
                     ▼
           ┌─────────────────────┐
           │ Initial Doc Gen AI  │
           │ (Gen tổng doc từ code)│
           └─────────┬───────────┘
                     ▼
           ┌─────────────────────┐
           │ Doc Chunk + Embedding│
           └─────────┬───────────┘
                     ▼
           ┌─────────────────────┐
           │ Vector DB (FAISS/ Qdrant)│
           └─────────┬───────────┘
                     │
   ┌─────────────────┴─────────────────┐
   │                                 │
   ▼                                 ▼
┌─────────────┐            ┌─────────────────────────┐
│Code Change  │            │ Semantic Search Vector DB│
│ (diff/git)  │───────────>│ → lấy đoạn doc liên quan  │
└─────┬───────┘            └───────────────┬─────────┘
      │                                      │
      ▼                                      ▼
 ┌─────────────┐                   ┌─────────────────┐
 │ AI Update   │                   │ AI Gen New Doc  │
 │ Doc đoạn liên quan             │ (module mới)    │
 └─────┬───────┘                   └────────┬────────┘
       │                                      │
       ▼                                      ▼
 ┌───────────────────────────────┐
 │ Doc Assembler + Flowchart Gen │
 └──────────────┬────────────────┘
                ▼
       ┌──────────────────────────┐
       │ Markdown Doc + Changelog  │
       └──────────────────────────┘
💸 Cost estimation & free status
Vector DB & code parsing: 100% free, chạy local

Embedding model: có nhiều model embedding free để dùng

AI call Gemini/OpenAI:

Gemini có free tier giới hạn (có thể dùng thận trọng, phân đoạn doc nhỏ)

OpenAI có free trial token, sau đó trả phí theo usage

Workflow diagram tools: free (Mermaid, PlantUML)

Tự host pipeline: free trên máy dev hoặc server nhỏ

Tổng thể: có thể build hoàn toàn free 100% nếu kiểm soát gọi API hợp lý, tối ưu prompt & chunk doc
