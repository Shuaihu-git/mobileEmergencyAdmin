package cn.xgs.domain.websocket;

import jakarta.websocket.Session;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketClient {
    /*
     * 与某个客户端的连接会话
     */
    private Session session;
    /*
    * 连接的uri
    **/
    private String uri;
}
