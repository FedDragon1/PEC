import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileCombiner {		//负责将每一x坐标的所有function在一个新的function中调用
	public static void main(String[] args) throws IOException {
		int end = 3694;										//最后一格特效的x轴坐标
		for (int xValue = 0; xValue <= end; xValue++) {		//从x轴0开始，
			String name = Integer.toString(xValue);						//文件名 = FuncNAME.mcfunction, 比如 Func1.mcfunction
			File OutputFunc = new File("Func" + name + ".mcfunction");
			FileWriter output = new FileWriter(OutputFunc);				//创建输出流
			String command = "function " + name;						//创建调用命令，比如 function 0
			output.write(command);										//在区间内的每一格至少都有一个特效，所以直接写入文件并换行
			output.write('\n');
			System.out.println(command);								//向控制台输出命令
			for (int func_suffix = 1; func_suffix <= 10; func_suffix++) {			//对于每一格，检测后缀1-10的文件是否存在（有的时候文件防重名会出bug，穷举比较妥当）
				File function = new File(name + '_' + func_suffix + ".mcfunction");	//文件 = NAME_FS.mcfunction, 比如 0_1.mcfunction
				if (function.exists()) {								//如果文件存在：
					command = "function " + name + '_' + func_suffix;		//调用那个function -> function 0_1
					output.write(command);									//写入文件并换行	
					output.write('\n');
					System.out.println(command);							//向控制台输出
				}
			}
			output.flush();				//刷新，关闭输出流
			output.close();
		}
	}
}
