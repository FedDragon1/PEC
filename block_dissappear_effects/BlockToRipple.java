package block_dissappear_effects;

import java.io.File;		//文件和数学库
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

public class BlockToRipple {
	//这个程序的逻辑可以被大量优化，但是我太懒了awa
	public static void ripple(double x, double y, double height, String effect_type, String particle_type) throws IOException {
		BigDecimal NameIdentifier1 = new BigDecimal(0);		//前程序中的desX
		BigDecimal NameIdentifier2 = new BigDecimal(-1);	//前程序中的desY
		BigDecimal AddConst = new BigDecimal(Double.toString(0));	//自增常量：
		BigDecimal OneOverFourty = new BigDecimal(Double.toString(0.025));	//要么是0.025
		BigDecimal OneOverTwenty = new BigDecimal(Double.toString(0.05));	//要么是0.05
		String setBlock = String.format("setblock %f %f %f air", x, height, y);	//setblock 给定坐标方块为空气
		double limit;	//文件生成限制
		if (effect_type == "flower") {	//如果是flower
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
		File function = new File(name + ".mcfunction");
		while (function.exists()) {		//当文件存在
			File cache = new File(name + '_' + func_suffix + ".mcfunction");	//x_suff.mcfunction
			function.renameTo(cache);	//将已经存在（更早）的文件重命名
			func_suffix++;			//func_suffix加一
		}
		FileWriter output = new FileWriter(function);	//对文件写入
		for (double i = 0; i < limit; i += 0.05) {	//Identifiers自增，循环20（或40）次
			if (effect_type == "flower") {	//如果是flower
				func_command = String.format("particle minecraft:%s_%s_%.3f_%.3f %.10f %.10f %.10f", 
						particle_type, effect_type, NameIdentifier1, NameIdentifier2, x + 0.5, height + 0.5, y + 0.5);
						//particle minecraft:PARTICLE_flower_IDENTIFIER1（三位小数）_IDENTIFIER2（三位小数） x+0.5 y+0.5 z+0.5
			} else {	//如果是其他的
				func_command = String.format("particle minecraft:%s_%s_%.2f_%.2f %.10f %.10f %.10f", 
						particle_type, effect_type, NameIdentifier1, NameIdentifier2, x + 0.5, height + 0.5, y + 0.5);
						//particle minecraft:PARTICLE_TYPE_IDENTIFIER1（两位小数）_IDENTIFIER2（两位小数） x+0.5 y+0.5 z+0.5
			}
			output.write(func_command + "\n");	//写入文件并换行
			System.out.println(func_command);	//向控制台输出命令
			System.out.println(k);		//向控制台输出总文件个数
			k++;	//k自增
			NameIdentifier2 = NameIdentifier2.add(AddConst);	//Identifiers自增
			NameIdentifier1 = NameIdentifier1.add(AddConst);
		}
		for (double g = 0; g < limit; g += 0.05) {	//Identifier2自增，Identifier1自减，循环20（或40）次
			if (effect_type == "flower") {	//如果是flower
				func_command = String.format("particle minecraft:%s_%s_%.3f_%.3f %.10f %.10f %.10f", 
						particle_type, effect_type, NameIdentifier1, NameIdentifier2, x + 0.5, height + 0.5, y + 0.5);
						//particle minecraft:PARTICLE_flower_IDENTIFIER1（三位小数）_IDENTIFIER2（三位小数） x+0.5 y+0.5 z+0.5
			} else {	//如果是其他的
				func_command = String.format("particle minecraft:%s_%s_%.2f_%.2f %.10f %.10f %.10f", 
						particle_type, effect_type, NameIdentifier1, NameIdentifier2, x + 0.5, height + 0.5, y + 0.5);
						//particle minecraft:PARTICLE_TYPE_IDENTIFIER1（两位小数）_IDENTIFIER2（两位小数） x+0.5 y+0.5 z+0.5
			}
			output.write(func_command + "\n");	//写入文件并换行
			System.out.println(func_command);	//向控制台输出命令
			System.out.println(k);	//向控制台输出总文件个数
			k++;	//k自增
			NameIdentifier2 = NameIdentifier2.add(AddConst);		//Identifier2自增
			NameIdentifier1 = NameIdentifier1.subtract(AddConst);	//Identifier1自减
		}
		for (double h = 0; h < limit; h += 0.05) {	//Identifiers自减，循环20（或40）次
			if (effect_type == "flower") {	//如果是flower
				func_command = String.format("particle minecraft:%s_%s_%.3f_%.3f %.10f %.10f %.10f", 
						particle_type, effect_type, NameIdentifier1, NameIdentifier2, x + 0.5, height + 0.5, y + 0.5);
						//particle minecraft:PARTICLE_flower_IDENTIFIER1（三位小数）_IDENTIFIER2（三位小数） x+0.5 y+0.5 z+0.5
			} else {	//如果是其他的
				func_command = String.format("particle minecraft:%s_%s_%.2f_%.2f %.10f %.10f %.10f", 
						particle_type, effect_type, NameIdentifier1, NameIdentifier2, x + 0.5, height + 0.5, y + 0.5);
						//particle minecraft:PARTICLE_TYPE_IDENTIFIER1（两位小数）_IDENTIFIER2（两位小数） x+0.5 y+0.5 z+0.5
			}
			output.write(func_command + "\n");	//写入文件并换行
			System.out.println(func_command);	//向控制台输出命令
			System.out.println(k);	//向控制台输出总文件个数
			k++;	//k自增
			NameIdentifier2 = NameIdentifier2.subtract(AddConst);	//Identifiers自减
			NameIdentifier1 = NameIdentifier1.subtract(AddConst);
		}
		for (double j = 0; j < limit; j += 0.05) {	//Identifier1自增，Identifier2自减，循环20（或40）次
			if (effect_type == "flower") {	//如果是flower
				func_command = String.format("particle minecraft:%s_%s_%.3f_%.3f %.10f %.10f %.10f", 
						particle_type, effect_type, NameIdentifier1, NameIdentifier2, x + 0.5, height + 0.5, y + 0.5);
						//particle minecraft:PARTICLE_flower_IDENTIFIER1（三位小数）_IDENTIFIER2（三位小数） x+0.5 y+0.5 z+0.5
			} else {	//如果是其他的
				func_command = String.format("particle minecraft:%s_%s_%.2f_%.2f %.10f %.10f %.10f", 
						particle_type, effect_type, NameIdentifier1, NameIdentifier2, x + 0.5, height + 0.5, y + 0.5);
						//particle minecraft:PARTICLE_TYPE_IDENTIFIER1（两位小数）_IDENTIFIER2（两位小数） x+0.5 y+0.5 z+0.5
			}
			output.write(func_command + "\n");	//写入文件并换行
			System.out.println(func_command);	//向控制台输出命令
			System.out.println(k);	//向控制台输出总文件个数
			k++;	//k自增
			NameIdentifier2 = NameIdentifier2.subtract(AddConst);	//Identifier2自减
			NameIdentifier1 = NameIdentifier1.add(AddConst);		//Identifier1自增
		}
		output.write(setBlock);	//写入setblock指令
		output.flush();	//刷新，关闭输出流
		output.close();
	}
	public static void main(String[] args) throws IOException {
		BlockToRipple.ripple(0, 5, 30, "circle", "endRod");
		//double x, double y, double height, String effect_type, String particle_type
	}
}
