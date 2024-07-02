package org.example.aidemo.application;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.example.aidemo.application.entity.AiDemoResult;
import org.example.aidemo.application.entity.PythonScriptParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AiDemoService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 执行 python 脚本
     *
     * @param param 执行 python 脚本的参数
     */
    public List<AiDemoResult> execPythonScript(String param) {
        log.info(param);
        String path = "temp.txt"; //toFile(param);
        FileWriter writer = null;
        try {
            writer = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(param);
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<AiDemoResult> results = new ArrayList<>();
        String cmd = "python demo.py";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            boolean isData = false;
            while ((line = in.readLine()) != null) {
                log.info("line text : {}", line);
                if ("save predictions done".equals(line)) {
                    isData = true;
                    continue;
                }
                if (isData) {
                    results.add(JSONObject.parseObject(line, AiDemoResult.class));
                }
            }
            in.close();
            int exitCode = proc.waitFor();
            log.info("exitCode = {}", exitCode);
        } catch (IOException | InterruptedException e) {
            log.error("运算错误。", e);
        }
        return results;
    }
}
