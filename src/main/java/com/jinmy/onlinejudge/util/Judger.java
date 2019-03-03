package com.jinmy.onlinejudge.util;

import com.jinmy.onlinejudge.config.Config;
import com.jinmy.onlinejudge.entity.Problem;
import com.jinmy.onlinejudge.entity.Solution;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Judger extends SandboxApi {
    protected Judger() {
    }

    @Autowired
    private Config config;

    /**
     * @param filename   source file
     * @param problem
     * @param solution
     * @param lang       Language Code 0-5 ==>  c,cpp,java,py2,py3
     * @param input_name input name, as for test 1.in, the input name is 1
     */
    public void init(String filename, Problem problem, Solution solution, int lang, String input_name) {
        super.init();
        run_dir = config.getSrc_dir() + solution.getId() + "/";
        uid = Integer.parseInt(config.getUid());
        gid = Integer.parseInt(config.getGid());
        max_cpu_time = problem.getTimeLimit();
        max_real_time = problem.getTimeLimit() + Math.max(max_cpu_time, 5);
        max_memory = problem.getMemoryLimit();
        max_stack = max_memory;
        switch (lang) {
            case 0:
                exe_path = run_dir + "main";
                seccomp = "c_cpp";
                args = "\" \"";
                break;
            case 1:
                exe_path = run_dir + "main";
                seccomp = "c_cpp";
                args = "\" \"";
                break;
            case 2:
                exe_path = "/usr/bin/java";
                max_cpu_time *= 2;
                max_real_time *= 2;
                max_memory *= 2;
                memory_limit_check_only = 1;
                max_stack *= 2;
                args = "-cp " + run_dir + " -XX:MaxRAM=" + problem.getMemoryLimit() / 1000 + "k " + config.getJudge().getJava().getArgs();
                args = args.replaceAll(" ", " --args=");
                seccomp = null;
                break;
            case 3:
                exe_path = "/usr/bin/python";
                seccomp = "general";
                args = run_dir + config.getJudge().getPython2().getArgs();
                break;
            case 4:
                exe_path = "/usr/bin/python3";
                args = run_dir + config.getJudge().getPython3().getArgs();
                args = args.replaceAll(" ", " --args=");
                //log.info("{}",args);
                seccomp = "general";
                break;
        }
        output_path = run_dir + input_name + ".out";
        input_path = config.getData_dir() + problem.getId() + "/" + input_name + ".in";
        log_path = run_dir + input_name + ".judgelog";
        script = String.format("%s " +
                        " --max_cpu_time=%d " +
                        " --max_real_time=%d " +
                        " --max_memory=%d" +
                        "  --memory_limit_check_only=%d " +
                        " --max_stack=%d " +
                        " --max_process_number=%d " +
                        " --max_output_size=%d " +
                        " --exe_path=%s " +
                        " --input_path=%s " +
                        "--log_path=%s" +
                        " --output_path=%s " +
                        " --error_path=%s " +
                        " --args=%s " +
                        " --uid=%s " +
                        " --gid=%s ",
                config.getJudger_dir(),
                max_cpu_time,
                max_real_time,
                max_memory,
                memory_limit_check_only,
                max_stack,
                max_process_number,
                max_output_size,
                exe_path,
                input_path,
                log_path,
                output_path,
                log_path,
                args,
                uid,
                gid);
        if (lang != 2)
            script += " --seccomp_rule_name=\"" + seccomp + "\" ";
    }
}
