async function login() {
    console.log('auth.js загружен'); // Сообщение для проверки загрузки скрипта

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const response = await fetch('/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            username: username,
            password: password,
        }),
    });

    if (response.ok) {
        const data = await response.json();
        localStorage.setItem('token', data.token); // Сохраняем токен
        window.location.href = '/chat'; // Перенаправляем в чат
    } else {
        alert('Неверные учетные данные');
    }
}
