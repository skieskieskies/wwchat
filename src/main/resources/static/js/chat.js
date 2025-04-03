let stompClient = null;

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        stompClient.subscribe('/topic/messages', (message) => {
            addMessage(JSON.parse(message.body));
        });

        stompClient.subscribe('/topic/users', (response) => {
            const users = JSON.parse(response.body);
            updateUsers(users);
        });

        stompClient.send("/app/request-users");

        fetch('/api/messages')
            .then(response => response.json())
            .then(messages => messages.forEach(addMessage));
    });
}
function sendMessage() {
    const input = document.getElementById('message-input');
    const message = {
        text: input.value
    };

    if (message.text.trim()) {
        stompClient.send("/app/send", {}, JSON.stringify(message));
        input.value = '';
    }
}

function addMessage(message) {
    const messagesDiv = document.getElementById('messages');
    const messageElement = document.createElement('div');
    messageElement.className = 'message';
    messageElement.innerHTML = `
        <strong>${message.author}</strong>: 
        ${message.text}
        <small>${new Date(message.timestamp).toLocaleTimeString()}</small>
    `;
    messagesDiv.appendChild(messageElement);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

function updateUsers(users) {
    const usersList = document.getElementById('users-list');
    const countSpan = document.getElementById('online-count');

    usersList.innerHTML = users
        .map(user => `<li>${user.nickname}</li>`)
        .join('');

    countSpan.textContent = users.length;
}

document.getElementById('message-input').addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        sendMessage();
    }
});

window.onload = connect;