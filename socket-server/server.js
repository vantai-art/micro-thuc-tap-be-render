/**
 * Socket.IO Server — Real-time cho e-commerce
 * Port: 5000
 *
 * Rooms:
 *   admin-room   → admin nhận mọi thông báo
 *   staff-room   → nhân viên nhận đơn mới từ khách
 *   user:{id}    → khách hàng nhận sync giỏ hàng & trạng thái đơn
 */

const express = require('express')
const http = require('http')
const { Server } = require('socket.io')

const app = express()
const server = http.createServer(app)

const ALLOWED_ORIGINS = [
    'http://localhost:3000',
    'http://localhost:8081',
    'http://localhost:19006',
    'http://127.0.0.1:3000',
    'http://127.0.0.1:8081',
    'http://10.0.2.2:8081',
    // ⚠️ Thay YOUR_FRONTEND_APP bằng tên app frontend thật của bạn
    'https://YOUR_FRONTEND_APP.onrender.com',
]

const io = new Server(server, {
    cors: {
        origin: ALLOWED_ORIGINS,
        methods: ['GET', 'POST'],
        credentials: true,
    },
    // Cho phép cả websocket lẫn polling để fallback
    transports: ['websocket', 'polling'],
})

// ─── Health check ─────────────────────────────────────────────────
app.get('/health', (_req, res) => res.json({ status: 'ok', port: 5000 }))

// ─── Socket.IO events ─────────────────────────────────────────────
io.on('connection', (socket) => {
    console.log(`✅ Client connected: ${socket.id}`)

    // ── Admin join ──────────────────────────────────────────────
    socket.on('join-admin', (data) => {
        socket.join('admin-room')
        console.log(`👑 Admin joined: ${data?.name || socket.id}`)
    })

    // ── Staff join ──────────────────────────────────────────────
    socket.on('join-staff', (data) => {
        socket.join('staff-room')
        if (data?.staffId) socket.join(`staff:${data.staffId}`)
        console.log(`👨‍💼 Staff joined: ${data?.name || socket.id}`)
    })

    // ── User join ───────────────────────────────────────────────
    socket.on('join-user', (data) => {
        if (data?.userId) {
            socket.join(`user:${data.userId}`)
            console.log(`👤 User joined room user:${data.userId}`)
        }
    })

    // ── Cart update (user thay đổi giỏ hàng) ───────────────────
    socket.on('cart-update', (data) => {
        if (data?.userId) {
            // Phát lại cho các tab khác của cùng user
            socket.to(`user:${data.userId}`).emit('cart-sync', data)
        }
    })

    // ── Staff thêm món vào giỏ của user ────────────────────────
    socket.on('staff-add-to-cart', (data) => {
        if (data?.userId) {
            io.to(`user:${data.userId}`).emit('staff-add-to-cart', data)
            console.log(`🛒 Staff added item for user:${data.userId}`)
        }
    })

    // ── User đặt đơn → thông báo staff + admin ─────────────────
    socket.on('user-order-placed', (data) => {
        io.to('staff-room').emit('new-user-order', data)
        io.to('admin-room').emit('new-user-order', data)
        console.log(`📦 New order from user:${data?.userId}`)
    })

    // ── Cập nhật trạng thái đơn hàng ───────────────────────────
    socket.on('order-status-update', (data) => {
        if (data?.userId) {
            io.to(`user:${data.userId}`).emit('order-status-update', data)
        }
        io.to('admin-room').emit('order-status-update', data)
        io.to('staff-room').emit('order-status-update', data)
    })

    // ── Admin cập nhật sản phẩm → broadcast cho mọi người ──────
    socket.on('product-updated', (data) => {
        socket.broadcast.emit('product-updated', data)
        console.log(`📦 Product updated: ${data?.action} - ${data?.product?.name}`)
    })

    // ── Disconnect ──────────────────────────────────────────────
    socket.on('disconnect', (reason) => {
        console.log(`❌ Client disconnected: ${socket.id} (${reason})`)
    })
})

// ─── Start ────────────────────────────────────────────────────────
// Render inject biến PORT tự động (thường là 10000)
// Dùng PORT trước, fallback về SOCKET_PORT hoặc 5000
const PORT = process.env.PORT || process.env.SOCKET_PORT || 5000
server.listen(PORT, () => {
    console.log(`🚀 Socket.IO server running on http://localhost:${PORT}
   → Truy cập qua API Gateway: ws://localhost:8080/socket.io`)
})
