# Plan for Trips - README 

## Giới thiệu dự án

**Plan for Trips** là một ứng dụng web giúp người dùng lập kế hoạch chi tiết cho một chuyến đi du lịch. Ứng dụng cung cấp các tính năng hỗ trợ người dùng dễ dàng lên lịch trình, phân bổ thời gian, quản lý các điểm đến, cũng như theo dõi các hoạt động trong chuyến đi. Mục tiêu chính của ứng dụng là giúp người dùng có cái nhìn tổng quan và quản lý hiệu quả thời gian của họ trong mỗi chuyến đi.

### Các tính năng chính:
- Tạo và quản lý các kế hoạch cho chuyến đi.
- Phân bổ thời gian và theo dõi các hoạt động dự kiến.
- Đề xuất các địa điểm và lịch trình hợp lý.
- Chia sẻ lịch trình với bạn bè và gia đình.

## Yêu cầu hệ thống

Để chạy và phát triển dự án này, bạn cần đảm bảo các yêu cầu hệ thống sau:

### Backend:
- **JDK**: Phiên bản 21
- **Maven**

### Frontend:
- **React**: Phiên bản 18
- **Bootstrap**: Để tạo giao diện người dùng đơn giản và responsive.

### Công cụ phát triển:
- **Visual Studio Code**: Sử dụng để phát triển và quản lý mã nguồn cho phần frontend.
- **IntelliJ IDEA**: Sử dụng để phát triển phần backend Java.

## Cài đặt và chạy dự án

### Chạy dự án frontend với Visual Studio Code:
1. Cài đặt Node.js và npm (Node Package Manager).
2. Clone repository dự án về máy:
   ```bash
   git clone https://github.com/ductienmt/fe-planfortrips.git
   ```
   ```bash
   git clone https://github.com/ductienmt/be-planfortrips.git
   ```
3. Mở project `frontend`:
4. Cài đặt các gói phụ thuộc:
   ```bash
   npm install
   ```
5. Chạy ứng dụng React:
   ```bash
   npm run dev
   ```
6. Ứng dụng sẽ được khởi chạy tại địa chỉ: `http://localhost:5050`.

### Chạy dự án backend với IntelliJ IDEA: 
1. Mở IntelliJ IDEA và import dự án backend:
    - Mở **IntelliJ**, chọn **Open**, sau đó chọn thư mục `backend` của dự án.
2. Đảm bảo JDK 21 đã được cài đặt.
3. Build và chạy ứng dụng Spring Boot từ IntelliJ.
4. API sẽ được khởi chạy tại địa chỉ `http://localhost:8080`.

## Đóng góp

Chúng tôi luôn hoan nghênh các ý tưởng và đóng góp từ cộng đồng. Nếu bạn muốn tham gia phát triển dự án, vui lòng fork repository và gửi pull request!

## License

Dự án này được cấp phép theo điều khoản của **MIT License**. Vui lòng tham khảo file `LICENSE` để biết thêm chi tiết.

---

Cảm ơn bạn đã quan tâm đến dự án **Plan for Trips**! ❤️
