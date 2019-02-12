package com.wx.somecode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    /**
     * SpringMVC调用Controller，Controller返回一个Callback对象
     * SpringMVC调用ruquest.startAsync并且将Callback提交到TaskExecutor中去执行
     * DispatcherServlet以及Filters等从应用服务器线程中结束，但Response仍旧是打开状态，也就是说暂时还不返回给客户端
     * TaskExecutor调用Callback返回一个结果，SpringMVC将请求发送给应用服务器继续处理
     * DispatcherServlet再次被调用并且继续处理Callback返回的对象，最终将其返回给客户端
     *
     * @return
     */
    @RequestMapping("/testCallable")
    public Callable<String> testCallable() {
        log.info("Controller开始执行！");
        Callable<String> callable = () -> {
            Thread.sleep(5000);

            log.info("实际工作执行完成！");

            return "succeed!";
        };
        log.info("Controller执行结束！");
        return callable;
    }

    /**
     * SpringMVC调用Controller，Controller返回一个DeferredResult对象
     * SpringMVC调用ruquest.startAsync
     * DispatcherServlet以及Filters等从应用服务器线程中结束，但Response仍旧是打开状态，也就是说暂时还不返回给客户端
     * 某些其它线程将结果设置到DeferredResult中，SpringMVC将请求发送给应用服务器继续处理
     * DispatcherServlet再次被调用并且继续处理DeferredResult中的结果，最终将其返回给客户端
     *
     */
    private DeferredResult<String> deferredResult = new DeferredResult<>();

    /**
     * 返回DeferredResult对象
     *
     * @return
     */
    @RequestMapping("/testDeferredResult")
    public DeferredResult<String> testDeferredResult() {
        return deferredResult;
    }

    /**
     * 对DeferredResult的结果进行设置
     * @return
     */
    @RequestMapping("/setDeferredResult")
    public String setDeferredResult() {
        deferredResult.setResult("Test result!" + new Random().nextInt());
        return "succeed";
    }

    /**
     * Callback和DeferredResult用于设置单个结果，如果有多个结果需要返回给客户端时，可以使用SseEmitter
     */
    private SseEmitter sseEmitter = new SseEmitter();

    /**
     * 返回SseEmitter对象
     *
     * @return
     */
    @RequestMapping("/testSseEmitter")
    public SseEmitter testSseEmitter() {
        return sseEmitter;
    }

    /**
     * 向SseEmitter对象发送数据
     *
     * @return
     */
    @RequestMapping("/setSseEmitter")
    public String setSseEmitter() {
        try {
            sseEmitter.send(System.currentTimeMillis());
        } catch (IOException e) {
            log.error("IOException!", e);
            return "error";
        }

        return "Succeed!";
    }

    /**
     * 将SseEmitter对象设置成完成
     *
     * @return
     */
    @RequestMapping("/completeSseEmitter")
    public String completeSseEmitter() {
        sseEmitter.complete();

        return "Succeed!";
    }

    @GetMapping("/download")
    public StreamingResponseBody handle() {
        return outputStream -> {
            for (int i = 0; i < 1000; ++i) {
                outputStream.write(i);
            }
        };
    }
}
