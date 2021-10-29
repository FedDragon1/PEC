import java.math.BigDecimal;	//更精确的数学库和文件相关库
import java.io.IOException;	
import java.io.File;
import java.io.FileWriter; 

public class SpiralLineExplained {
	public static void drawSpiralLine(double x1, double x2, double y1, double y2, double height, String particle_type, int count) throws IOException {
						//定义方法，比之前的两个程序多了一个count，用于判断是两条螺旋线还是一条
		BigDecimal RotateConstant = new BigDecimal(0.05);	//使用更精确的小数库，定义旋转常数（后面会用）
		BigDecimal yDestination = new BigDecimal(0);		//定义粒子生成后的移动方向（y轴，mc中z轴）
		BigDecimal hDestination = new BigDecimal(-1);		//定义粒子生成后的移动方向（高度轴）
		double distance = x2 - x1;							//计算x轴上的距离，斜率，平移常数
		double slope = (y2 - y1) / (x2 - x1);
		double startX = x1;
		double startY = y1;
		double k = 1;				//防重名
		int flag = 1;				//flag用于判定yDestination和hDestination是否使用相反数（双螺旋）
		for(double t = 0; t < distance; t++) {						//文件层循环，生成所有文件
			String name = Integer.toString((int) (x1 + t));			//文件名
			File function = new File(name + ".mcfunction");			//文件名+.mcfunction后缀（生成文件）
			while (function.exists()) {										//防重名
				File replacement = new File(name + '_' + (int)(k) + ".mcfunction");
				function.renameTo(replacement);
				k += 1 / distance;
			}
			FileWriter output = new FileWriter(function);			//创建输出，向上面生成的文件写入
			for(double i = 0; i < 1; i += 0.025) {							//粒子循环层，生成一份文件内的粒子，注意每格现在算40次
				String func_command;										//先定义指令，方便在if外调用
				double y = slope * i + startY + 0.5;						//算单个粒子的y轴坐标（mc的z轴）
				double x = startX + i + 0.5;								//算单个粒子的x轴坐标
				if (flag == 1) {													//判定flag，第一次判定一定为True
					func_command = String.format("particle minecraft:%s_vertical_%.2f_%.2f %.10f %.10f %.10f", particle_type, yDestination, hDestination, x, height + 0.5, y);
																			//如果flag是1，那么直接输出指令
																			//将会生成一条螺线
										//particle minecraft:TYPE_verticle_yDES（两位小数）_hDES（两位小数） X Y Z
				} else {
					func_command = String.format("particle minecraft:%s_vertical_%.2f_%.2f %.10f %.10f %.10f", particle_type, yDestination.negate(), hDestination.negate(), x, height + 0.5, y);
																			//如果flag不是1，那么输出指令（yDes和hDes取反）
																			//将会生成两条盘旋的螺线
										//particle minecraft:TYPE_verticle_-yDES（两位小数）_-hDES（两位小数） X Y Z
				}
				output.write(func_command + '\n'); 		//将命令写入文件
				System.out.println(func_command);		//输出命令到控制台
																//destination数值不代表真实移动到的相对坐标
				if ((startX - x1) % 2 == 0 && i < 0.5) {		//顺时针画圈，x每前进两格画一圈，从(-1, 0)开始，如果是每两格的前0.5格：
					yDestination = yDestination.add(RotateConstant);	//yDes + 0.05（圆圈左上部分）
					hDestination = hDestination.add(RotateConstant);	//hDes + 0.05
				} else if ((startX - x1) % 2 == 0 && i >= 0.5) {	//如果是每两格的0.5-1格：
					yDestination = yDestination.subtract(RotateConstant);	//yDes - 0.05（圆圈右上部分）
					hDestination = hDestination.add(RotateConstant);		//hDes - 0.05
				} else if ((startX - x1) % 2 == 1 && i < 0.5) {		//如果是每两格的1-1.5格：
					yDestination = yDestination.subtract(RotateConstant);	//yDes - 0.05（圆圈右下部份）
					hDestination = hDestination.subtract(RotateConstant);	//hDes - 0.05
				} else {											//其他情况（每两格的最后0.5格）：
					yDestination = yDestination.add(RotateConstant);		//yDes + 0.05（圆圈左下部分）
					hDestination = hDestination.subtract(RotateConstant);	//hDes + 0.05
				}
				if (count == 2) {		//如果是双螺旋
					flag = 0 - flag;		//那么flag取反（下次判定flag时选择与上次不同的那个）
				}
			}
			startX++;				//下一格的开始时x值
			startY += slope;		//下一格的开始时y值
			output.flush();			//刷新，关闭输出流
			output.close();
		}
	}
	public static void main(String[] args) throws IOException {
		SpiralLineExplained.drawSpiralLine(10, 30, 5, 20, 30, "endRod", 2); 
													//调用方法，利用自定义含有初速度的粒子用线的方式连接两个点
													//粒子生成之后移动形成螺线
	}
}
