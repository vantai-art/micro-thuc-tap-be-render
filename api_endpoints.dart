/// api_endpoints.dart
///
/// Cách dùng:
///   - Dev local:    flutter run
///   - Production:   flutter run --dart-define=ENV=production
///   - Build prod:   flutter build apk --dart-define=ENV=production
///
/// Hoặc set trong launch.json:
///   "args": ["--dart-define=ENV=production"]

class ApiEndpoints {
  ApiEndpoints._();

  // ─── Base URLs ────────────────────────────────────────────────
  static const String _env =
      String.fromEnvironment('ENV', defaultValue: 'development');

  static const String _devBase = 'http://10.0.2.2:8080'; // Android emulator
  // static const String _devBase = 'http://localhost:8080'; // Web/Desktop
  // static const String _devBase = 'http://192.168.x.x:8080'; // Physical device

  static const String _prodBase =
      'https://ecom-api-gateway-phoe.onrender.com'; // ← URL thật của bạn

  static const String _devSocket = 'http://10.0.2.2:5000';
  static const String _prodSocket = 'https://ecom-socket-server.onrender.com';

  static bool get isProduction => _env == 'production';

  static String get baseUrl => isProduction ? _prodBase : _devBase;
  static String get socketUrl => isProduction ? _prodSocket : _devSocket;

  // ─── AUTH / USER SERVICE ──────────────────────────────────────
  static String get login => '$baseUrl/login';
  static String get register => '$baseUrl/registration';
  static String get users => '$baseUrl/users';
  static String userById(int id) => '$baseUrl/users/$id';
  static String get roles => '$baseUrl/roles';
  static String get forgotPassword => '$baseUrl/auth/forgot-password';
  static String get resetPassword => '$baseUrl/auth/reset-password';

  // ─── PRODUCT CATALOG ─────────────────────────────────────────
  static String get products => '$baseUrl/products';
  static String productById(int id) => '$baseUrl/products/$id';
  static String get adminProducts => '$baseUrl/admin/products';
  static String adminProductById(int id) => '$baseUrl/admin/products/$id';
  static String get uploadImage => '$baseUrl/admin/products/upload-image';
  static String get activityLogs => '$baseUrl/admin/activity-logs';

  // ─── RECOMMENDATIONS ─────────────────────────────────────────
  static String get recommendations => '$baseUrl/recommendations';
  static String recommendationsByUser(int userId) =>
      '$baseUrl/$userId/recommendations';

  // ─── ORDER SERVICE ────────────────────────────────────────────
  static String get orders => '$baseUrl/order';
  static String orderById(int id) => '$baseUrl/order/$id';
  static String get cart => '$baseUrl/cart';
  static String cartByUser(int userId) => '$baseUrl/cart/$userId';
  static String get tables => '$baseUrl/tables';
  static String tableById(int id) => '$baseUrl/tables/$id';
  static String get bills => '$baseUrl/bills';
  static String billById(int id) => '$baseUrl/bills/$id';

  // ─── PAYMENT SERVICE ─────────────────────────────────────────
  static String get payments => '$baseUrl/api/payments';
  static String get vnpayCreate => '$baseUrl/api/payments/vnpay/create';
  static String get vnpayReturn => '$baseUrl/api/payments/vnpay/return';
  static String get revenue => '$baseUrl/api/revenue';
  static String revenueByDate(String from, String to) =>
      '$baseUrl/api/revenue?from=$from&to=$to';

  // ─── SETTING SERVICE ─────────────────────────────────────────
  static String get settings => '$baseUrl/api/settings';
  static String settingByKey(String key) => '$baseUrl/api/settings/$key';

  // ─── SOCKET.IO ───────────────────────────────────────────────
  // Dùng socketUrl trực tiếp với socket_io_client package:
  // final socket = IO.io(ApiEndpoints.socketUrl, <String, dynamic>{
  //   'transports': ['websocket', 'polling'],
  //   'autoConnect': false,
  // });
}
