package org.example.aidemo.interfaces;

import org.example.aidemo.application.AiDemoService;
import org.example.aidemo.application.entity.AiDemoResult;
import org.example.aidemo.application.entity.PythonScriptParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai-demo")
public class AiDemoController {
    private final AiDemoService aiDemoService;


    public AiDemoController(AiDemoService aiDemoService) {
        this.aiDemoService = aiDemoService;
    }

    /**
     * 执行 ai
     *
     * @param param 执行 ai 的参数
     * @return 返回 ai 结果
     */
    @PostMapping(value = "/exec", produces = "application/json;charset=UTF-8")
    public List<AiDemoResult> getPythonScript(@RequestBody String param) {
        return this.aiDemoService.execPythonScript(param);
    }
}
