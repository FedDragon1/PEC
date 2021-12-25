package pec.blockDisappearEffects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class Disappear {
	static PrintWriter output;		//对文件写入
	public static void ripple(double x, double y, double height, String effectType, String particleType) throws IOException {
		File directory = new File(".\\Pec Output");
		directory.mkdir();
		BigDecimal NameIdentifier1 = new BigDecimal(0);		//前程序中的desX
		BigDecimal NameIdentifier2 = new BigDecimal(-1);	//前程序中的desY
		BigDecimal AddConst = new BigDecimal(Double.toString(0));	//自增常量：
		BigDecimal OneOverFourty = new BigDecimal(Double.toString(0.025));	//要么是0.025
		BigDecimal OneOverTwenty = new BigDecimal(Double.toString(0.05));	//要么是0.05
		String setBlock = String.format("setblock %f %f %f air", x, height, y);	//setblock 给定坐标方块为空气
		double limit;	//文件生成限制
		if (effectType == "flower") {	//如果是flower
			limit = 1.96;	//文件生成限制40份每for循环，无误差情况下为1.95（1.95/0.05+1=40)
			AddConst = OneOverFourty;	//NameIdentifier的自增常量为0.025
		} else {	//剩下的
			limit = 0.96;	//文件生成限制20份每for循环，无误差情况下为0.95（0.95/0.05+1=20)
			AddConst = OneOverTwenty;	//NameIdentifier的自增常量为0.05
		}
		int k = 1;	//debug变量，用于检测总文件数
		int func_suffix = 1;	//重名后缀
		String name = Integer.toString((int)(x));	//x.mcfunction
		String func_command = "";
		File function = new File("Pec Output\\" + name + ".mcfunction");
		while (function.exists()) {		//当文件存在
			File cache = new File("Pec Output\\" + name + '_' + func_suffix + ".mcfunction");	//x_suff.mcfunction
			function.renameTo(cache);	//将已经存在（更早）的文件重命名
			func_suffix++;			//func_suffix加一
		}
		output = new PrintWriter(new BufferedWriter(new FileWriter(function)), true);
		for (double i = 0; i < limit; i += 0.05) {	//Identifiers自增，循环20（或40）次
			if (effectType == "flower") {	//如果是flower
				func_command = String.format("particle minecraft:%s_%s_%.3f_%.3f %.10f %.10f %.10f", 
						particleType, effectType, NameIdentifier1, NameIdentifier2, x + 0.5, height + 0.5, y + 0.5);
						//particle minecraft:PARTICLE_flower_IDENTIFIER1（三位小数）_IDENTIFIER2（三位小数） x+0.5 y+0.5 z+0.5
			} else {	//如果是其他的
				func_command = String.format("particle minecraft:%s_%s_%.2f_%.2f %.10f %.10f %.10f", 
						particleType, effectType, NameIdentifier1, NameIdentifier2, x + 0.5, height + 0.5, y + 0.5);
						//particle minecraft:PARTICLE_TYPE_IDENTIFIER1（两位小数）_IDENTIFIER2（两位小数） x+0.5 y+0.5 z+0.5
			}
			output.println(func_command);	//写入文件并换行
			System.out.println(func_command);	//向控制台输出命令
			System.out.println(k);		//向控制台输出总文件个数
			k++;	//k自增
			NameIdentifier2 = NameIdentifier2.add(AddConst);	//Identifiers自增
			NameIdentifier1 = NameIdentifier1.add(AddConst);
		}
		for (double g = 0; g < limit; g += 0.05) {	//Identifier2自增，Identifier1自减，循环20（或40）次
			if (effectType == "flower") {	//如果是flower
				func_command = String.format("particle minecraft:%s_%s_%.3f_%.3f %.10f %.10f %.10f", 
						particleType, effectType, NameIdentifier1, NameIdentifier2, x + 0.5, height + 0.5, y + 0.5);
						//particle minecraft:PARTICLE_flower_IDENTIFIER1（三位小数）_IDENTIFIER2（三位小数） x+0.5 y+0.5 z+0.5
			} else {	//如果是其他的
				func_command = String.format("particle minecraft:%s_%s_%.2f_%.2f %.10f %.10f %.10f", 
						particleType, effectType, NameIdentifier1, NameIdentifier2, x + 0.5, height + 0.5, y + 0.5);
						//particle minecraft:PARTICLE_TYPE_IDENTIFIER1（两位小数）_IDENTIFIER2（两位小数） x+0.5 y+0.5 z+0.5
			}
			output.println(func_command);	//写入文件并换行
			System.out.println(func_command);	//向控制台输出命令
			System.out.println(k);	//向控制台输出总文件个数
			k++;	//k自增
			NameIdentifier2 = NameIdentifier2.add(AddConst);		//Identifier2自增
			NameIdentifier1 = NameIdentifier1.subtract(AddConst);	//Identifier1自减
		}
		for (double h = 0; h < limit; h += 0.05) {	//Identifiers自减，循环20（或40）次
			if (effectType == "flower") {	//如果是flower
				func_command = String.format("particle minecraft:%s_%s_%.3f_%.3f %.10f %.10f %.10f", 
						particleType, effectType, NameIdentifier1, NameIdentifier2, x + 0.5, height + 0.5, y + 0.5);
						//particle minecraft:PARTICLE_flower_IDENTIFIER1（三位小数）_IDENTIFIER2（三位小数） x+0.5 y+0.5 z+0.5
			} else {	//如果是其他的
				func_command = String.format("particle minecraft:%s_%s_%.2f_%.2f %.10f %.10f %.10f", 
						particleType, effectType, NameIdentifier1, NameIdentifier2, x + 0.5, height + 0.5, y + 0.5);
						//particle minecraft:PARTICLE_TYPE_IDENTIFIER1（两位小数）_IDENTIFIER2（两位小数） x+0.5 y+0.5 z+0.5
			}
			output.println(func_command);	//写入文件并换行
			System.out.println(func_command);	//向控制台输出命令
			System.out.println(k);	//向控制台输出总文件个数
			k++;	//k自增
			NameIdentifier2 = NameIdentifier2.subtract(AddConst);	//Identifiers自减
			NameIdentifier1 = NameIdentifier1.subtract(AddConst);
		}
		for (double j = 0; j < limit; j += 0.05) {	//Identifier1自增，Identifier2自减，循环20（或40）次
			if (effectType == "flower") {	//如果是flower
				func_command = String.format("particle minecraft:%s_%s_%.3f_%.3f %.10f %.10f %.10f", 
						particleType, effectType, NameIdentifier1, NameIdentifier2, x + 0.5, height + 0.5, y + 0.5);
						//particle minecraft:PARTICLE_flower_IDENTIFIER1（三位小数）_IDENTIFIER2（三位小数） x+0.5 y+0.5 z+0.5
			} else {	//如果是其他的
				func_command = String.format("particle minecraft:%s_%s_%.2f_%.2f %.10f %.10f %.10f", 
						particleType, effectType, NameIdentifier1, NameIdentifier2, x + 0.5, height + 0.5, y + 0.5);
						//particle minecraft:PARTICLE_TYPE_IDENTIFIER1（两位小数）_IDENTIFIER2（两位小数） x+0.5 y+0.5 z+0.5
			}
			output.println(func_command);	//写入文件并换行
			System.out.println(func_command);	//向控制台输出命令
			System.out.println(k);	//向控制台输出总文件个数
			k++;	//k自增
			NameIdentifier2 = NameIdentifier2.subtract(AddConst);	//Identifier2自减
			NameIdentifier1 = NameIdentifier1.add(AddConst);		//Identifier1自增
		}
		output.print(setBlock);	//写入setblock指令
		output.close();
	}

	public static void fade(double x, double y, double height, String type, String particle /*not used only for format  (Excel)*/) throws IOException {
		File directory = new File(".\\Pec Output");
		directory.mkdir();
		BigDecimal i = new BigDecimal(0);					//for循环穷举用变量
		final BigDecimal OneTenth = new BigDecimal(0.1);	//常量0.1
		final BigDecimal One = new BigDecimal(1);			//常量1
		final BigDecimal Zero = new BigDecimal(0);			//常量1=0
		final Apfloat aHalf = new Apfloat(0.5);		//常量0.5
		final BigDecimal Half = new BigDecimal(0.5);
		BigDecimal deltaBx = new BigDecimal(x);			//BigDecimal类的x，粒子所在的x坐标
		final BigDecimal Bx = new BigDecimal(x);		//BigDecimal类的x常量
		BigDecimal deltaBy = new BigDecimal(y);			//BigDecimal类的y，粒子所在的y坐标
		final BigDecimal By = new BigDecimal(y);		//BigDecimal类的y常量
		BigDecimal deltaBh = new BigDecimal(height);	//BigDecimal类的高度，粒子所在的高度
		final BigDecimal Bh = new BigDecimal(height);	//BigDecimal类的高度常量
		Apfloat deltaAx = new Apfloat(x);			//Apfloat类的x，粒子所在的x坐标
		final Apfloat Ax = new Apfloat(x);			//Apfloat类的x常量
		Apfloat deltaAy = new Apfloat(y);			//Apfloat类的y，粒子所在的y坐标
		final Apfloat Ay = new Apfloat(y);			//Apfloat类的y常量
		Apfloat deltaAh = new Apfloat(height);		//Apfloat类的高度，粒子所在的高度
		final Apfloat Ah = new Apfloat(height);		//Apfloat类的高度常量
		final BigDecimal TAU = new BigDecimal(Double.toString(2 * Math.PI));	//常量2PI
		final BigDecimal step = new BigDecimal(Double.toString(Math.PI / 30));	//常量pi/30，60份文件每2pi
		
		
		
		String setBlock = String.format("setblock %f %f %f air", x, height, y);	
												//将指定坐标方块设为空气
		int func_suffix = 1;		//防重名
		String name = Integer.toString((int)(x));		//文件名=x.mcfunction
		File function = new File("Pec Output\\" + name + ".mcfunction");	//创建新文件
		while (function.exists()) {	//当文件重名
			File cache = new File("Pec Output\\" + name + '_' + func_suffix + ".mcfunction");
												//文件名=x_suff.mcfunction
			function.renameTo(cache);			//将原来存在的文件重名成上面的文件名
			func_suffix += 1;					//suff自增
		}
		output = new PrintWriter(new BufferedWriter(new FileWriter(function)), true);	//创建输出流
		switch(type) {	//如果type是...
			case "cube":	//正方体，则：
				for(i.add(Zero); i.compareTo(One) < 0; i = i.add(OneTenth)) {	//循环十遍：
																		//等同于for(i + 0; i < 1; i += 0.1)
					//原坐标上对x轴穷举（第一条棱）
					deltaBx = Bx.add(i);	//x轴的坐标随着i的增加而增加
					deltaBy = By;			//y轴的坐标保持相同
					deltaBh = Bh;			//高度保持相同
					String func_command = String.format("particle float %.10f %.10f %.10f", deltaBx, deltaBh, deltaBy);	
											//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
					
					//原坐标高度+1，对x轴穷举（第二条棱）
					deltaBx = Bx.add(i);	//x轴的坐标随着i的增加而增加
					deltaBy = By;			//y轴的坐标保持相同
					deltaBh = Bh.add(One);	//高度+1
					func_command = String.format("particle float %.10f %.10f %.10f", deltaBx, deltaBh, deltaBy);	
											//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
					
					//原坐标y轴坐标+1，对x轴穷举（第三条棱）
					deltaBx = Bx.add(i);	//x轴的坐标随着i的增加而增加
					deltaBy = By.add(One);	//y轴的坐标+1
					deltaBh = Bh;			//高度保持相同
					func_command = String.format("particle float %.10f %.10f %.10f", deltaBx, deltaBh, deltaBy);	
											//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
					
					//原坐标高度和y轴坐标各+1，对x轴穷举（第四条棱）
					deltaBx = Bx.add(i);	//x轴的坐标随着i的增加而增加
					deltaBy = By.add(One);	//y轴的坐标+1
					deltaBh = Bh.add(One);	//高度+1
					func_command = String.format("particle float %.10f %.10f %.10f", deltaBx, deltaBh, deltaBy);	
											//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
					
					//原坐标，对y轴（mc的z轴）穷举（第五条棱）
					deltaBx = Bx;			//x轴坐标不变
					deltaBy = By.add(i);	//y轴的坐标随着i的增加而增加
					deltaBh = Bh;			//高度不变
					func_command = String.format("particle float %.10f %.10f %.10f", deltaBx, deltaBh, deltaBy);	
											//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
					
					//原坐标x轴坐标+1，对y轴穷举（第六条棱）
					deltaBx = Bx.add(One);	//x轴坐标=1
					deltaBy = By.add(i);	//y轴的坐标随着i的增加而增加
					deltaBh = Bh;			//高度不变
					func_command = String.format("particle float %.10f %.10f %.10f", deltaBx, deltaBh, deltaBy);	
											//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
					
					//原坐标高度+1，对y轴穷举（第七条棱）
					deltaBx = Bx;			//x轴坐标不变
					deltaBy = By.add(i);	//y轴的坐标随着i的增加而增加
					deltaBh = Bh.add(One);	//高度+1
					func_command = String.format("particle float %.10f %.10f %.10f", deltaBx, deltaBh, deltaBy);	
											//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);;	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
					
					//原坐标高度和x轴坐标各+1，对y轴穷举（第八条棱）
					deltaBx = Bx.add(One);	//x轴坐标+1
					deltaBy = By.add(i);	//y轴的坐标随着i的增加而增加
					deltaBh = Bh.add(One);	//高度+1
					func_command = String.format("particle float %.10f %.10f %.10f", deltaBx, deltaBh, deltaBy);	
											//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
					
					//原坐标，对高度穷举（第九条棱）
					deltaBx = Bx;			//x轴坐标不变
					deltaBy = By;			//y轴的坐标不变
					deltaBh = Bh.add(i);	//高度随着i的增加而增加
					func_command = String.format("particle float %.10f %.10f %.10f", deltaBx, deltaBh, deltaBy);	
											//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
					
					//原坐标x轴+1，对高度穷举（第十条棱）
					deltaBx = Bx.add(One);	//x轴坐标+1
					deltaBy = By;			//y轴的坐标不变
					deltaBh = Bh.add(i);	//高度随着i的增加而增加
					func_command = String.format("particle float %.10f %.10f %.10f", deltaBx, deltaBh, deltaBy);	
											//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
					
					//原坐标y轴+1，对高度穷举（第十一条棱）
					deltaBx = Bx;			//x轴坐标不变
					deltaBy = By.add(One);	//y轴的坐标+1
					deltaBh = Bh.add(i);	//高度随着i的增加而增加
					func_command = String.format("particle float %.10f %.10f %.10f", deltaBx, deltaBh, deltaBy);	
											//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
					
					//原坐标x轴和y轴各+1，对高度穷举（第十二条棱）
					deltaBx = Bx.add(One);	//x轴坐标+1
					deltaBy = By.add(One);	//y轴坐标+1
					deltaBh = Bh.add(i);	//高度随着i的增加而增加
					func_command = String.format("particle float %.10f %.10f %.10f", deltaBx, deltaBh, deltaBy);	
											//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);;	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
				}
				String extra_command1 = String.format("particle float %.10f %.10f %.10f",Bx.add(One),Bh.add(One),By.add(One));
													//生成补充指令，将三个轴+1的粒子补上（穷举最多到0.9）
				output.print(extra_command1);		//将补充指令写入文件
				System.out.println(extra_command1);	//向控制台输出
				break;	//不再进入下一个case，switch结束
			
			case "octahedron":	//如果是八面体：
				for (i.add(Zero); i.compareTo(TAU) < 0; i = i.add(step)) {	//step = pi/30
					Apfloat aT = new Apfloat(i);	//参数方程中的参数，用于凹面八面体
					
					//第一个四角星，在xy平面上
					deltaAx = ApfloatMath.pow(ApfloatMath.sin(aT), 3).add(Ax);	//x = sin^3(t) + Ax
					deltaAy = ApfloatMath.pow(ApfloatMath.cos(aT), 3).add(Ay);	//y = cos^3(t) + Ay
					deltaAh = Ah;	//高度保持相同
					String func_command = String.format("particle float2 %.10f %.10f %.10f", deltaAx.add(aHalf).doubleValue(), deltaAh.add(aHalf).doubleValue(), deltaAy.add(aHalf).doubleValue());
														//这里使用float2因为图形较大，要使用更高上升速率的粒子。+0.5是因为不加会将三轴坐标小的那个顶点当作中心点
					output.println(func_command);	//向文件写入命令并换行
					System.out.println(func_command);	//向控制台输出
					
					//第二个四角星，在xh平面上
					deltaAx = ApfloatMath.pow(ApfloatMath.sin(aT), 3).add(Ax);	//x = sin^3(t) + Ax
					deltaAy = Ay;	//y轴坐标保持相同
					deltaAh = ApfloatMath.pow(ApfloatMath.cos(aT), 3).add(Ah);	//h = cos^3(t) + Ah
					func_command = String.format("particle float2 %.10f %.10f %.10f", deltaAx.add(aHalf).doubleValue(), deltaAh.add(aHalf).doubleValue(), deltaAy.add(aHalf).doubleValue());
														//这里使用float2因为图形较大，要使用更高上升速率的粒子。+0.5是因为不加会将三轴坐标小的那个顶点当作中心点
					output.println(func_command);;	//向文件写入命令并换行
					System.out.println(func_command);	//向控制台输出
					
					//第三个四角星，在yh平面上
					deltaAx = Ax;	//x轴坐标保持相同
					deltaAy = ApfloatMath.pow(ApfloatMath.sin(aT), 3).add(Ay);	//y = cos^3(t) + Ay
					deltaAh = ApfloatMath.pow(ApfloatMath.cos(aT), 3).add(Ah);	//h = cos^3(t) + Ah
					func_command = String.format("particle float2 %.10f %.10f %.10f", deltaAx.add(aHalf).doubleValue(), deltaAh.add(aHalf).doubleValue(), deltaAy.add(aHalf).doubleValue());
														//这里使用float2因为图形较大，要使用更高上升速率的粒子。+0.5是因为不加会将三轴坐标小的那个顶点当作中心点
					output.println(func_command);	//向文件写入命令并换行
					System.out.println(func_command);	//向控制台输出
				}
				break;
				
			case "tetrahedron": //是四面体：
				final BigDecimal NegSqrt3Over6 = new BigDecimal(Math.sqrt(3) / -6);
				final BigDecimal NegSqrt3Over3 = new BigDecimal(Math.sqrt(3) / -3);
				final BigDecimal Sqrt3Over6 = new BigDecimal(Math.sqrt(3) / 6);
				final BigDecimal Sqrt3Over3 = new BigDecimal(Math.sqrt(3) / 3);
				final BigDecimal Sqrt6Over3 = new BigDecimal(Math.sqrt(6) / 3);
				final BigDecimal OnePointFive = new BigDecimal(1.5);
				final BigDecimal NegPointFive = new BigDecimal(-0.5);
				for (i.add(Zero); i.compareTo(One) < 0; i = i.add(OneTenth)) {
					//(-v3/6, -0.5, 0) to (-v3/6, 0.5, 0) 第一条棱
					deltaBx = OnePointFive.multiply(NegSqrt3Over6).add(Bx);			//x = 1.5 * -v3/6 + Bx
					deltaBy = OnePointFive.multiply(NegPointFive.add(i)).add(By);	//y = 1.5 * (-0.5 + i) + By
					deltaBh = Bh;	//高度保持相同（1.5 * 0）
					String func_command = String.format("particle float %.10f %.10f %.10f", deltaBx.add(Half), deltaBh.add(Half), deltaBy.add(Half));	
									//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
					
					//(-v3/6, -0.5, 0) to (v3/3, 0, 0) 第二条棱
					deltaBx = OnePointFive.multiply(i.multiply(Sqrt3Over3.subtract(NegSqrt3Over6)).add(NegSqrt3Over6)).add(Bx);	
											//x = 1.5 * (i * (v3/3 - -v3/6) + -v3/6) + Bx
					deltaBy = OnePointFive.multiply(i.multiply(Half).add(NegPointFive)).add(By);	
											//y = 1.5 * (i * 0.5 + -0.5) +By
					deltaBh = Bh;	//高度保持相同（1.5 * 0）
					func_command = String.format("particle float %.10f %.10f %.10f", deltaBx.add(Half), deltaBh.add(Half), deltaBy.add(Half));	
					//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
					
					//(-v3/6, 0.5, 0) to (v3/3, 0, 0) 第三条棱
					deltaBx = OnePointFive.multiply(i.multiply(Sqrt3Over3.subtract(NegSqrt3Over6)).add(NegSqrt3Over6)).add(Bx);	
											//x = 1.5 * (i * (v3/3 - -v3/6) + -v3/6) + Bx
					deltaBy = OnePointFive.multiply(i.multiply(NegPointFive).add(Half)).add(By);	
											//y = 1.5 * (i * -0.5 + 0.5) +By
					deltaBh = Bh;	//高度保持相同（1.5 * 0）
					func_command = String.format("particle float %.10f %.10f %.10f", deltaBx.add(Half), deltaBh.add(Half), deltaBy.add(Half));	
					//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
					
					//(v3/3, 0, 0) to (0, 0, v6/3) 第四条棱
					deltaBx = OnePointFive.multiply(i.multiply(NegSqrt3Over3).add(Sqrt3Over3)).add(Bx);	
											//x = 1.5 * (i * -v3/3 + v3/3) + Bx
					deltaBy = By;	//y轴保持相同
					deltaBh = OnePointFive.multiply(i.multiply(Sqrt6Over3)).add(Bh);
											//h = 1.5 * i * v6/3 + Bh
					func_command = String.format("particle float %.10f %.10f %.10f", deltaBx.add(Half), deltaBh.add(Half), deltaBy.add(Half));	
					//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令 
					
					//(-v3/6, -0.5, 0) to (0, 0, v6/3) 第五条棱
					deltaBx = OnePointFive.multiply(i.multiply(Sqrt3Over6).add(NegSqrt3Over6)).add(Bx);	
											//x = 1.5 * (i * v3/6 - v3/6) + Bx
					deltaBy = OnePointFive.multiply(i.multiply(Half).add(NegPointFive)).add(By);			
											//y = 1.5 * (i * 0.5 - 0.5) + By
					deltaBh = OnePointFive.multiply(i.multiply(Sqrt6Over3)).add(Bh);
											//h = 1.5 * i * v6/3 + Bh
					func_command = String.format("particle float %.10f %.10f %.10f", deltaBx.add(Half), deltaBh.add(Half), deltaBy.add(Half));	
					//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
					
					//(-v3/6, 0.5, 0) to (0, 0, v6/3) 第六条棱
					deltaBx = OnePointFive.multiply(i.multiply(Sqrt3Over6).add(NegSqrt3Over6)).add(Bx);	
											//x = 1.5 * (i * v3/6 - v3/6) + Bx
					deltaBy = OnePointFive.multiply(i.multiply(NegPointFive).add(Half)).add(By);			
											//y = 1.5 * (i * -0.5 + 0.5) + By
					deltaBh = OnePointFive.multiply(i.multiply(Sqrt6Over3)).add(Bh);
											//h = 1.5 * i * v6/3 + Bh
					func_command = String.format("particle float %.10f %.10f %.10f", deltaBx.add(Half), deltaBh.add(Half), deltaBy.add(Half));	
					//命令 = particle float deltaBx deltaBh deltaBy
					output.println(func_command);	//写入命令并换行
					System.out.println(func_command);	//向控制台输出命令
				}
				String extra_command2 = String.format("particle float %.10f %.10f %.10f", Bx.add(Half),Bh.add(Sqrt6Over3.multiply(OnePointFive)).add(Half),By.add(Half));
									//生成补充指令，将顶部的粒子补上，高度=1.5 * v6/3 +Bh
				output.print(extra_command2);		//将补充指令写入文件
				System.out.println(extra_command2);	//向控制台输出
				break;	//不再进入下一个case，switch结束
			
			case "sphere":
				String func_command;	//命令输出变量
				final Apfloat Two = new Apfloat(2); 							//常数2
				final Apfloat aOne = new Apfloat(1);							//常数1
				final Apfloat phi = new Apfloat((Math.sqrt(5) - 1) / 2, 16);	//黄金分割比（0.618)
				final BigDecimal TotalParticles = new BigDecimal(120); 			//总粒子数=120
				BigDecimal NowParticle = new BigDecimal(1);						//现在正在生成第几个粒子（1）
				final Apfloat aTotal = new Apfloat(TotalParticles, 16);	//总粒子数（Apfloat）
				for (NowParticle.add(Zero); NowParticle.compareTo(TotalParticles) <= 0; NowParticle = NowParticle.add(One)) {
									//等同于 for (NowParticle; NowParticle <= TotalParticles; NowParticle++)
					Apfloat aNow = new Apfloat(NowParticle, 16);	//相当于参数t
					deltaAh = (Two.multiply(aNow).subtract(aOne)).divide(aTotal).subtract(aOne);	
								//h = (2n - 1) / N - 1; n = aNow, N = aTotal
					deltaAx = ApfloatMath.sqrt(aOne.subtract(ApfloatMath.pow(deltaAh, 2))).multiply
							(ApfloatMath.cos(Two.multiply(ApfloatMath.pi(16)).multiply(aNow).multiply(phi))).add(Ax);
								//x = v(1 - h^2) * cos(2pi * n * phi) + Ax
					deltaAy = ApfloatMath.sqrt(aOne.subtract(ApfloatMath.pow(deltaAh, 2))).multiply
							(ApfloatMath.sin(Two.multiply(ApfloatMath.pi(16)).multiply(aNow).multiply(phi))).add(Ay);
								//x = v(1 - h^2) * sin(2pi * n * phi) + Ay
					deltaAh = deltaAh.add(Ah);
								//h += Ah
					func_command = String.format("particle float2 %.10f %.10f %.10f", 
							deltaAx.add(aHalf).doubleValue(), deltaAh.add(aHalf).doubleValue(), deltaAy.add(aHalf).doubleValue());
								//particle float2 OutputX+0.5 OutputH+0.5 OutputY+0.5	（漂浮更高粒子）
					output.println(func_command);
					System.out.println(func_command);
				}
				break;
				
			default:
				System.out.println("输入错误");
				
		}
		output.print('\n' + setBlock);	//写入setblock指令
		output.close();
	}
	public static void main(String[] args) throws IOException {
		Disappear.fade(20, -10, 30, "cube", null);
		//Disappear.ripple(20, -10, 30, "flower", "endRod");
	}
}
