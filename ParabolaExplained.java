import java.io.IOException;		//文件相关库
import java.io.File;
import java.io.FileWriter; 

public class ParabolaExplained {
	public static void drawParabola(double x1, double x2, double y1, double y2, double height, String particle_type) throws IOException {
		double distance = x2 - x1;				//文件数量（“静距离”）
		double slope = (y2 - y1) / (x2 - x1);	//xy平面斜率
		double startX = x1;						//储存这一个文件的x起始坐标（平移粒子到正确点）
		double startY = y1;						//储存这一个文件的y起始坐标（平移粒子到正确点）
		double relativeX = 0;					//x轴移动了的相对距离（平移粒子到正确点，高度）
		double a = -(2 / distance);				//二次项系数 = -2/静距离
		double k = 1;							//防重名
		for (double t = 0; t < distance; t++) {							//生成文件
			String name = Integer.toString((int)(t + x1));				//定义文件名
			File function = new File(name + ".mcfunction");				//生成新的文件
			while (function.exists()) {													//防重名
				File replacement = new File(name + '_' + (int)(k) + ".mcfunction");			//生成临时文件（只用文件名）
				function.renameTo(replacement);												//重命名已经存在的文件
				k += 1 / distance;														//如果还重名就全都重命名成 name_k+1.mcfunction
			}
			FileWriter output = new FileWriter(function);				//文件输出流
			for (double i = 0; i < 1; i += 0.05) {										//以每个文件20条粒子指令穷举粒子坐标
				double x = startX + i + 0.5;											//x轴坐标 = startX.i+0.5  startX为整数而i为小数
				double y = startY + slope * i + 0.5;									//y轴坐标 = 上一个文件的记录 + 增加值 + 0.5；每一个文件y轴自增斜率值
				double h = a * Math.pow(i + relativeX, 2) + 2 * (i + relativeX) + height + 0.5;	
																						//高度坐标 = ax^2+2x, x为相对于程序开始时x轴偏移量，为relativeX.i; 再将结果平移至指定高度+0.5
				String func_command = String.format("particle minecraft:%s %.10f %.10f %.10f",particle_type, x, h, y);	
																						//定义输出命令，将得到的三个轴坐标值带入命令
				output.write(func_command + "\n");										//向文件写入命令并换行
				System.out.println(func_command);										//向控制台输出命令
			}
			startX++;													//更新函数，为下一次运算做初始化
			relativeX++;
			startY += slope;
			output.flush();												//刷新输出流，关闭输出流
			output.close();
		}
	}
	public static void main(String[] args) throws IOException {
		ParabolaExplained.drawParabola(10, 30, 5, 20, 30, "endRod");	//调用方法，用endRod粒子（自定义）以线的方式连接(10, 30, 5)和(30, 30, 20)
	}
}
