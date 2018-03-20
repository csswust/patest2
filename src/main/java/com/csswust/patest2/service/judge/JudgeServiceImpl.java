package com.csswust.patest2.service.judge;

import com.csswust.patest2.service.JudgeService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 杨顺丰
 */
@Service
public class JudgeServiceImpl implements JudgeService {
	@Override
	public JudgeTask getTaskBySubmId(Integer submId) {
		return null;
	}

	@Override
	public Map<String, Object> refresh(JudgeTask judgeTask, JudgeResult judgeResult) {
		return null;
	}

	@Override
	public JudgeResult judge(JudgeTask judgeTask) {
		JudgeResult judgeResult = new JudgeResult();
		String fileName = null;
		/*try {
			// 获得判题源文件的文件名
			fileName = Config.fileNameMap.get(judgeTask.getLanguage());
			// 创建源文件并写入代码
			FileUtil.generateFile(judgeTask.getSource(), Config.sourcePath, fileName);
			// 构建命令行命令
			StringBuffer cmd = new StringBuffer();
			cmd.append("python").append(" ").append(Config.scriptPath).append(Config.scriptName)
					.append(" ").append(judgeTask.getPid()).append(" ")
					.append(judgeTask.getTestdataNum()).append(" ").append(judgeTask.getLanguage())
					.append(" ").append(judgeTask.getLimitTime()).append(" ")
					.append(judgeTask.getLimitMemory()).append(" ")
					.append(judgeTask.getJudgeMode());
			// 执行
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmd.toString());
			// 获得错误信息
			String errMsg = StreamUtil.output(proc.getErrorStream());
			// 获得控制台信息
			String consoleMsg = StreamUtil.output(proc.getInputStream());
			System.out.println(judgeTask + "\n" + errMsg + "\n" + consoleMsg);
			judgeResult.setErrMsg(errMsg);
			judgeResult.setConsoleMsg(consoleMsg);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 删除源文件，如果没有执行将会导致判题系统堵塞
			FileUtil.removeFile(Config.sourcePath, fileName);
		}*/
		return judgeResult;
	}
}
