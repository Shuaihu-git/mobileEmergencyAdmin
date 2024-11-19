package cn.xgs.controller.websocket;

import cn.xgs.domain.response.ServerEncoder;
import cn.xgs.domain.vo.LogVo;
import cn.xgs.domain.websocket.WebSocketClient;
import cn.xgs.service.ISnowAccessService;
import cn.xgs.utils.SpringUtils;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * @author Xiaohu
 *
 */

@ServerEndpoint(value = "/websocket/{userName}", encoders = {ServerEncoder.class})
@Component
@Slf4j
public class WebSocketService {
    @Autowired
    private ISnowAccessService snowAccessService= SpringUtils.getBean("snowAccessService");

    private static ApplicationContext applicationContext;


    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    private static ConcurrentHashMap<String, WebSocketClient> webSocketMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, LogVo> messagesSendToAdmin = new ConcurrentHashMap<>();


    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userName
     */
    private String userName;


    /**
     * 客户端与服务端连接成功
     *
     * @param session 与某个客户端的连接会话
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) {
        this.session = session;
        //根据token获取用户名
        this.userName = userName;
        WebSocketClient client = new WebSocketClient();
        client.setSession(session);
        client.setUri(session.getRequestURI().toString());
        webSocketMap.put(userName, client);
        log.info("连接成功！"+session.getRequestURI().toString());
        log.info("连接成功！"+session);
    }

    /**
     * 客户端与服务端连接关闭
     */
    @OnClose
    public void onClose(String userName) {
        webSocketMap.remove(userName);
        log.info("连接关闭！");
    }

    /**
     * 客户端与服务端连接异常
     *
     * @param error 错误
     * @param session 会话
     */
    @OnError
    public void onError(Throwable error, Session session) {
    }

    /**
     * 客户端向服务端发送消息
     *
     * @param message 消息
     * @throws IOException 异常
     */
    @OnMessage
    public void onMsg(Session session, String message) throws IOException {
        log.info("消息到达{}",message);
        if (!message.isBlank()||!message.isEmpty()){
            if (webSocketMap.containsKey("admin")){
                cn.xgs.domain.dto.SnowAccessLog snowAccessLog = new cn.xgs.domain.dto.SnowAccessLog();
                snowAccessLog.setData(message);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                snowAccessLog.setTime(simpleDateFormat.format(new Date()));
                snowAccessLog.setTag("测试");
                snowAccessService.insert(snowAccessLog);
                sendMessage("admin",message);
            }
            messagesSendToAdmin.put("admin",new LogVo(message));
        }

    }

    public static void sendVo(String userName, LogVo vo) {
        try {
            WebSocketClient webSocketClient = webSocketMap.get(userName);
            if (webSocketClient != null) {
                webSocketClient.getSession().getBasicRemote().sendObject(vo);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (EncodeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 向指定客户端发送消息
     *
     * @param message 消息
     */
    public static void sendMessage(String userName, String message) {
        try {
            log.info("准备发消息！"+userName);
            WebSocketClient webSocketClient = webSocketMap.get(userName);
            log.info("连接成功"+webSocketMap);
            if (webSocketClient != null) {
                log.info("发送的消息是！"+message);
                webSocketClient.getSession().getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("失败");
            throw new RuntimeException(e.getMessage());
        }
    }
    @Scheduled(fixedRate = 1000 * 10)
    public void pushAdminWebSocket() {
        log.info("处理消息滞留重发问题");
        if (webSocketMap.contains("admin")) {
            Stream<Map.Entry<String, LogVo>> stream = messagesSendToAdmin.entrySet().stream();
            stream.forEach(e ->sendMessage(e.getKey(),e.getValue().getData()));
        }
    }

}

