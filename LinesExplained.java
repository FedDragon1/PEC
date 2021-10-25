import java.io.IOException;		//文件相关库
import java.io.File;
import java.io.FileWriter; 

public class LinesExplained {
	public static void drawline(double x1, double x2, double y1, double y2, double height, String particle_type) throws IOException {
												//定义方法，x1, x2为两点x值（x2需大于x1); y1, y2为两点z值（对应mc）；height则为高度（mc的y值）
												//方法缺点：只能沿x轴正方向穷举，(x2 - x1)不能为0，高度只能是固定值
		double distance = x2 - x1; 						//求出两点x值差，也就是总文件个数
		double slope = (y2 - y1) / (x2 - x1); 			//求出这条一次方程的斜率
		double k = 1;									//防止文件重名用变量
		double startX = x1;								//储存这一个文件的x起始坐标
		double startY = y1;								//储存这一个文件的y起始坐标
		for (double t = 0; t < distance; t++) {					//循环生成文件，文件个数=两点x值差
			String name = Integer.toString((int)(x1 + t));		//创建文件名本体，"x"（x=当前文件x轴坐标）
			File function = new File(name + ".mcfunction");		//创建新的文件，名称使用上一行定义的文件名加上mcfunction后缀
			while (function.exists()) {											//当文件重名
				File replacement = new File(name + '_' + (int)(k) + ".mcfunction");	//新的文件，名称为"x_[k].mcfunction"（k向下取整）
				function.renameTo(replacement);										//将重名的文件（原来的）重命名为刚才生成的文件
				k += 1 / distance;												//让k在算完一条线的长度之后+1，避免一条线的k值持续增加
			}
			FileWriter output = new FileWriter(function);		//创建新的输出流，写向刚才的文件
			for (double i = 0; i < 1; i += 0.05) {								//利用i/λ穷举，令λ为20（每一格生成20个粒子），所以i自增0.05；小于1因为仅限于这个方块
				double y = slope * i + startY + 0.5;							//y=斜率*x坐标；x坐标为i值，利用y起始坐标将粒子平移至正确的位置，再+0.5让粒子在方块正中心
				double x = i + startX + 0.5;									//x=i+x起始坐标
																				//如果没有起始坐标，所有粒子将重叠在一格空间内，因为0<=i<1; 如果没有0.5则会在方块的角落上（带小数点的坐标不自动+0.5）
				String func_command = String.format("particle minecraft:%s %.10f %.10f %.10f", particle_type, x, height + 0.5, y);
																				//定义命令格式，利用str.format规避科学计数（其实mc支持）
																				//str.format就是把后面的变量带入%s（字符串）和%.10f（10位小数）
				output.write(func_command + '\n');								//将命令写入文件，并换行
				System.out.println(func_command);								//在控制台输出定义的命令
			}
			startY += slope;									//定义下一次循环的起始坐标，y值经过一格穷举增加斜率值
			startX++;											//x固定增加1
			output.flush();										//刷新，关闭输出流
			output.close();
		}
	}
	public static void main(String[] args) throws IOException {
		LinesExplained.drawline(10, 30, 5, 20, 30, "endRod");	//调用方法，用endRod粒子（自定义）以线的方式连接(10, 30, 5)和(30, 30, 20)
	}
	
}
