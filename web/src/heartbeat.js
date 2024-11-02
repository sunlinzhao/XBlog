// src/plugins/heartbeat.js
import instance from '@/http';

export default {
    install() {
        const heartbeatInterval = 60000; // 60秒

        const getUserIp = async () => {
            try {
                const response = await fetch('https://api.ipify.org?format=json');
                const data = await response.json();
                return data.ip;
            } catch (error) {
                console.error('获取IP地址失败', error);
                return null;
            }
        };

        const sendHeartbeat = async () => {
            const userId = JSON.parse(sessionStorage.getItem('userData')).userId; // 从 sessionStorage 获取用户ID
            const ip = await getUserIp(); // 获取用户的IP地址

            if (userId && ip) {
                try {
                    const response = await instance.post('/xblog/user/heartbeat', {
                        userId: userId,
                        ip: ip,
                    });

                    if (response.data.success) {
                        // 心跳成功，可以在这里处理成功的逻辑
                    } else {
                        console.error('发送心跳失败:');
                    }
                } catch (error) {
                    console.error('发送心跳失败:', error);
                }
            }
        };
        if(sessionStorage.getItem('userData')) // 登录后才发送心跳
            // 每隔一定时间发送心跳
            setInterval(sendHeartbeat, heartbeatInterval);
    }
};
