package pec.customParticleGenerator;

import java.io.File;				//文件库
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.math.BigDecimal;		//数学库
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class SpiralLine {
	public static void vertical() throws IOException {
		Apfloat destinationH = new Apfloat(0);									//这两个变量用于定义文件名，而非真正粒子的目的地
		Apfloat destinationY = new Apfloat(1.0);								
		final BigDecimal Tau = new BigDecimal(Double.toString(2 * Math.PI));		//2PI
		BigDecimal t = new BigDecimal(0);										//定义t=0，接下来将使用t值计算粒子真正的目的地
		final BigDecimal step = new BigDecimal(Double.toString(Math.PI / 40));		//定义t增量，t最大值为2pi所以pi/40代表会生成80份不同的文件
		final BigDecimal Zero = new BigDecimal(0);									//常量0
		final BigDecimal Four = new BigDecimal(4);									//常量4
		final BigDecimal Two = new BigDecimal(2);									//常量2
		final BigDecimal Three = new BigDecimal(3);									//常量3
		final Apfloat OneOverTwenty = new Apfloat(0.05);							//常量0.05
		int i = 1;	//debug用变量
		int g = 1;
		int h = 1;
		int k = 1;
		
		for (t.add(Zero); (t.compareTo(Tau) <= 0); t = t.add(step)) {		//等同于 for(t+0; t<=2PI; t+=PI/40), +0是因为直接用t会报错
			System.out.println(t);											//debug用，输出t值
			Apfloat aT = new Apfloat(t);									//将t从BigDecimal转成Apfloat，以便使用三角函数
			BigDecimal bh = new BigDecimal((ApfloatMath.sin(aT)).toString());		//真正的desH = sin(t), 计算之后转回BigDecimal，写文件时Apfloat会使用科学计数法（其实mc能读，debug方便）
			BigDecimal by = new BigDecimal((ApfloatMath.cos(aT)).toString());		//真正的desY = cos(t), 同样转回BigDecimal
			String name = String.format("endRod_Vertical_%.2f_%.2f", destinationH.doubleValue(), destinationY.doubleValue());
																					//文件名称：endRod_Vertical_desX（两位小数）_desY（两位小数）
			File function = new File(name + ".json");						//生成文件，后缀为json
			FileOutputStream fos = new FileOutputStream(function);  		//用UTF-8对文件写入
			Writer output = new OutputStreamWriter(fos, "UTF-8");  
			String identifier = String.format("\"identifier\": \"minecraft:endRod_vertical_%.2f_%.2f\",", destinationH.doubleValue(), destinationY.doubleValue());
																			//这一行负责在游戏中调用粒子的名字，我设置成与文件名相同
			output.write("{" + '\n' + "  ");								//逐行写入文件
			output.write("\"format_version\": \"1.10.0\"," + '\n' + "  ");
			output.write("\"particle_effect\":{" + '\n' + "    ");
			output.write("\"description\":{" + '\n' + "      ");
			output.write(identifier + '\n' + "      ");						//写入粒子识别码
			output.write("\"basic_render_parameters\": {" + '\n' + "        ");
			output.write("\"material\": \"particles_blend\"," + '\n' + "        ");
			output.write("\"texture\": \"textures/particle/particles2\"" + '\n' + "      ");
			output.write("}" + '\n' + "    ");
			output.write("},\"components\":{" + '\n' + "      ");
			output.write("\"minecraft:emitter_rate_manual\": {" + '\n' + "        ");
			output.write("\"max_particles\": 50" + '\n' + "      ");
			output.write("}," + '\n' + "      ");
			output.write("\"minecraft:emitter_lifetime_once\": {" + '\n' + "        ");
			output.write("\"active_time\": 10" + '\n' + "      ");
			output.write("}," + '\n' + "      ");
			output.write("\"minecraft:minecraft:emitter_shape_point\": {" + '\n' + "      ");
			output.write("}," + '\n' + "      ");
			output.write("\"minecraft:particle_lifetime_expression\": {" + '\n' + "        ");
			output.write("\"max_lifetime\": 2" + '\n' + "      ");			//粒子生命为两秒钟
			output.write("}," + '\n' + "      ");  
			output.write("\"minecraft:particle_motion_parametric\": {" + '\n' + "        ");
			output.write("\"relative_position\": [" + '\n' + "          ");
			output.write("\"0.00\"," + '\n' + "          ");
			output.write("\"" + bh + " * Math.pow(variable.particle_age, 0.25)\"," + '\n' + "          ");
																			//以 √√x的速率，将粒子移动到正确的高度坐标
																			//这代表粒子最终会落到相对粒子生成约1.189格远的位置
			output.write("\"" + by + " * Math.pow(variable.particle_age, 0.25)\"" + '\n' + "        ");
																			//相同的速率，将粒子移动到正确的y轴（mc的z轴）坐标
			output.write("]" + '\n' + "      ");
			output.write("}," + '\n' + "      ");
			output.write("\"minecraft:particle_appearance_billboard\": {" + '\n' + "        ");
			output.write("\"size\": [0.1, 0.1]," + '\n' + "        ");
			output.write("\"facing_camera_mode\": \"lookat_xyz\"," + '\n' + "        ");
			output.write("\"uv\": {" + '\n' + "          ");				//自定义粒子翻书材质
			output.write("\"texture_width\": 960," + '\n' + "          ");
			output.write("\"texture_height\": 656," + '\n' + "          ");
			output.write("\"flipbook\": {" + '\n' + "            ");
			output.write("\"base_UV\": [944, 0]," + '\n' + "            ");
			output.write("\"size_UV\": [16, 16]," + '\n' + "            ");
			output.write("\"step_UV\": [-16, 0]," + '\n' + "            ");
			output.write("\"frames_per_second\": 8," + '\n' + "            ");
			output.write("\"max_frame\": 60," + '\n' + "            ");
			output.write("\"stretch_to_lifetime\": true," + '\n' + "            ");
			output.write("\"loop\": false" + '\n' + "          ");
			output.write("}" + '\n' + "        ");
			output.write("}" + '\n' + "      ");
			output.write("}," + '\n' + "      ");
			output.write("\"minecraft:particle_appearance_tinting\": {" + '\n' + "        ");
			output.write("\"color\": [ \"variable.particle_age > (variable.particle_lifetime / 2.0) ? 1 - (0.0153 * (1 - Math.pow(0.7, variable.particle_age)) / (1 - 0.7)) : 1.0\", "
							+ "\"variable.particle_age > (variable.particle_lifetime / 2.0) ? 1 - (0.0387 * (1 - Math.pow(0.7, variable.particle_age)) / (1 - 0.7)) : 1.0\", "
							+ "\"variable.particle_age > (variable.particle_lifetime / 2.0) ? 1 - (0.0636 * (1 - Math.pow(0.7, variable.particle_age)) / (1 - 0.7)) : 1.0\", "
							+ "\"variable.particle_age > (variable.particle_lifetime / 2.0) ? 1 - 0.60 * "
							+ "((variable.particle_age - (variable.particle_lifetime / 2.0)) / (variable.particle_lifetime / 2.0)) : 1.0\" ]" + '\n' + "      ");
			output.write("}" + '\n' + "    ");
			output.write("}" + '\n' + "  ");
			output.write("}" + '\n');
			output.write("}");
			output.flush();
			output.close();
			if (t.compareTo(Tau.divide(Four)) <= 0) {		//如果是前四分之一（圆的右上角）；等同于 if (t <= PI / 2)
				destinationH = destinationH.add(OneOverTwenty);			//desX += 0.05
				destinationY = destinationY.subtract(OneOverTwenty);	//desY -= 0.05
				System.out.println(i);	//向控制台输出这是右上角第几次生成文件
				i++;	//i自增
			} else if (t.compareTo(Tau.divide(Two)) <= 0) {	//如果是前四分之一到一半（圆的右下角）；等同于 if (t <= PI)
				destinationH = destinationH.subtract(OneOverTwenty);	//desX -= 0.05
				destinationY = destinationY.subtract(OneOverTwenty);	//desY -= 0.05
				System.out.println(g);	//向控制台输出这是右下角第几次生成文件
				g++;	//g自增
			} else if (t.compareTo(Tau.divide(Four).multiply(Three)) <= 0) {	//如果是圆的一半到3/4（圆的左下角）；等同于 if (t <= 1.5 * PI)
				destinationH = destinationH.subtract(OneOverTwenty);	//desX -= 0.05
				destinationY = destinationY.add(OneOverTwenty);			//desY += 0.05
				System.out.println(h);	//向控制台输出这是坐左下角第几次生成文件
				h++;	//h自增
			} else {										//如果是圆的最后1/4（圆的左上角）
				destinationH = destinationH.add(OneOverTwenty);			//desX += 0.05
				destinationY = destinationY.add(OneOverTwenty);			//desY += 0.05
				System.out.println(k);	//向控制台输出这是左上角第几次生成文件
				k++;	//k自增
			}
		}
	}
	public static void main(String[] args) throws IOException {
		SpiralLine.vertical();
	}
}
