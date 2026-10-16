# 🍳 PantryAI – Trợ lý nấu ăn thông minh

**PantryAI** là ứng dụng Android hỗ trợ người dùng tìm kiếm công thức nấu ăn, khám phá các món ăn Việt Nam và nhận gợi ý món ăn thông qua trí tuệ nhân tạo (AI). Ứng dụng hướng đến việc giúp người dùng tận dụng nguyên liệu sẵn có, tiết kiệm thời gian tìm kiếm và đơn giản hóa việc lựa chọn món ăn hằng ngày.

## 📌 Giới thiệu dự án

- **Tên dự án:** PantryAI
- **Nền tảng:** Android
- **Ngôn ngữ lập trình:** Kotlin
- **Giao diện:** Jetpack Compose
- **Kiến trúc:** Phân tách giao diện, ViewModel, Repository và tầng dữ liệu
- **Dependency Injection:** Hilt
- **Cơ sở dữ liệu cục bộ:** Room
- **Tích hợp AI:** Firebase AI Logic – Gemini
- **Quản lý mã nguồn:** Git và GitHub

## ✨ Chức năng chính

### 1. 📖 Tra cứu công thức nấu ăn

- Hiển thị danh sách các công thức nấu ăn.
- Cung cấp dữ liệu các món ăn Việt Nam.
- Tìm kiếm món ăn theo tên.
- Hỗ trợ tìm kiếm tiếng Việt không dấu.
- Lọc món ăn theo danh mục, độ khó và thời gian nấu.
- Hỗ trợ tìm kiếm và gợi ý công thức từ dữ liệu có sẵn.

### 2. 🤖 Trợ lý nấu ăn AI

- Tích hợp Gemini thông qua Firebase AI Logic.
- Cho phép người dùng nhập câu hỏi liên quan đến nấu ăn và món ăn.
- Hỗ trợ gợi ý món ăn dựa trên yêu cầu của người dùng.
- Kết hợp dữ liệu công thức cục bộ để đưa ra gợi ý thay thế khi cần thiết.
- Xử lý các tình huống lỗi khi giao tiếp với dịch vụ AI.

### 3. 💬 Quản lý cuộc trò chuyện

- Lưu lịch sử trò chuyện bằng Room.
- Xem lại các cuộc hội thoại trước đó.
- Quản lý nhiều cuộc trò chuyện.
- Xóa nội dung trò chuyện khi cần.
- Tách biệt logic xử lý hội thoại khỏi giao diện người dùng.

### 4. 🔎 Tìm kiếm và gợi ý thông minh

- Chuẩn hóa văn bản tiếng Việt để hỗ trợ tìm kiếm không dấu.
- Kết hợp nhiều tiêu chí để xếp hạng công thức phù hợp.
- Tận dụng dữ liệu công thức cục bộ để cải thiện trải nghiệm khi AI không khả dụng.

## 🛠️ Công nghệ sử dụng

| Công nghệ | Mục đích |
|---|---|
| Kotlin | Ngôn ngữ lập trình chính |
| Jetpack Compose | Xây dựng giao diện Android |
| MVVM | Tổ chức logic giao diện |
| ViewModel | Quản lý trạng thái giao diện |
| Repository Pattern | Tách biệt logic truy xuất và xử lý dữ liệu |
| Hilt | Dependency Injection |
| Room | Lưu trữ dữ liệu cục bộ |
| Kotlin Coroutines / Flow | Xử lý tác vụ bất đồng bộ và luồng dữ liệu |
| Firebase AI Logic | Tích hợp mô hình Gemini |
| Git / GitHub | Quản lý phiên bản mã nguồn |

## 🏗️ Cấu trúc dự án

Cấu trúc có thể được tổ chức theo các tầng chức năng như sau:

```

com.example.pantryai/
   ├── data/
        ├── local/
              ├── dao/
              ├── entity/
        ├── mock/
        ├── model/
        └── repository/
   ├── di/
   ├── navigation/
   ├── ui/
   │   ├── components/
   │   ├── screens/
   │   └── theme/
   ├── viewModel/
   └── MainActivity

```


## 🚀 Cài đặt và chạy dự án

### Yêu cầu

- Android Studio phiên bản tương thích với cấu hình Gradle của dự án.
- JDK tương thích với Android Gradle Plugin đang sử dụng.
- Android SDK theo cấu hình dự án.
- Tài khoản Firebase và cấu hình Firebase AI Logic nếu muốn sử dụng chức năng AI.

### Bước 1: Tải mã nguồn

```bash
git clone <URL_REPOSITORY>
cd PantryAI
```

Thay `<URL_REPOSITORY>` bằng địa chỉ repository GitHub của dự án.

### Bước 2: Cấu hình Firebase

1. Tạo hoặc mở dự án trên Firebase Console.
2. Đăng ký ứng dụng Android với package name `com.example.pantryai`.
3. Tải file `google-services.json`.
4. Đặt file tại thư mục `app/google-services.json`.
5. Bật và cấu hình Firebase AI Logic theo yêu cầu của dự án.
6. Đồng bộ Gradle trong Android Studio.

**Lưu ý bảo mật:** File `google-services.json` được sử dụng cục bộ và không nên tự ý thêm vào Git nếu dự án đã cấu hình loại trừ file này. Không đưa API key, token hoặc thông tin bí mật vào mã nguồn công khai. Khi triển khai thực tế, cần cấu hình Firebase App Check phù hợp với môi trường phát hành.

### Bước 3: Biên dịch dự án

Mở Terminal tại thư mục gốc dự án và chạy:

```powershell
.\gradlew assembleDebug
```

Nếu quá trình biên dịch thành công, Android Studio có thể chạy ứng dụng trên thiết bị Android hoặc trình giả lập tương thích.

### Bước 4: Chạy ứng dụng

1. Mở dự án bằng Android Studio.
2. Chờ Gradle Sync hoàn tất.
3. Chọn thiết bị Android hoặc emulator.
4. Nhấn **Run** để cài đặt và khởi chạy ứng dụng.

## 🔐 Lưu ý về Firebase và dữ liệu

- Cần có cấu hình Firebase hợp lệ để sử dụng chức năng AI.
- Chức năng AI phụ thuộc vào kết nối mạng, hạn mức sử dụng và tình trạng dịch vụ.
- Dữ liệu công thức cục bộ có thể được sử dụng để hỗ trợ tìm kiếm và gợi ý món ăn.
- Không công khai các tệp cấu hình hoặc thông tin xác thực nhạy cảm trên GitHub.
- Cần kiểm thử các tình huống mất mạng, lỗi dịch vụ và phản hồi không hợp lệ.

## 🎯 Mục tiêu học tập

Dự án PantryAI được xây dựng nhằm thực hành và củng cố các kiến thức phát triển ứng dụng Android hiện đại:

- Xây dựng giao diện bằng Jetpack Compose.
- Tổ chức ứng dụng theo MVVM và Repository Pattern.
- Quản lý trạng thái giao diện bằng ViewModel.
- Sử dụng Room để lưu trữ dữ liệu cục bộ.
- Áp dụng Hilt để quản lý dependency.
- Xử lý bất đồng bộ bằng Coroutines và Flow.
- Tích hợp dịch vụ AI vào ứng dụng Android.
- Quản lý mã nguồn và phát triển tính năng bằng Git branch.

## 👨‍💻 Tác giả
Trương Quang Việt Hoàng
GitHub: @Viethoang2005

**PantryAI** – Dự án thực hành phát triển ứng dụng Android với Kotlin, Jetpack Compose và AI.

---

*Nếu thấy dự án hữu ích, bạn có thể để lại một ⭐ trên repository GitHub.*
