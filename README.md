



<p align="center">

<a href="https://github.com/FedDragon/PEC">

<img src="https://raw.githubusercontent.com/FedDragon1/PEC/main/logo/PEC.png">

</a>

</p>

[![Licence: MIT (shields.io)](https://img.shields.io/badge/Licence-MIT-blueviolet)](http://choosealicense.com/licenses/mit/) [![Bilibili: 乐观的辣条 (shields.io)](https://img.shields.io/badge/Bilibili-%E4%B9%90%E8%A7%82%E7%9A%84%E8%BE%A3%E6%9D%A1-blueviolet)](https://space.bilibili.com/509754182)  
  
PCE is some easy Java programs that could help you to visualize music in Minecraft Bedrock.  
It is specificly deigned for Minecraft Windows 10. The file created could be used in other platform in theory, however, Windows 10 is recommend.
  
PCE 可以帮助您在《我的世界基岩版》制作特效红石音乐。  
推荐在 Windows 10 环境下进行制作，利用PCE生成的文件在其他平台理论上可用。
  
## Installation / 下载与安装  
> **NOTE**: I have no idea of how to make packages and ``` .jar ``` file. In order to PCE, you have to download and have the ```.java``` files in your working environment then execute them with an external file  
>  
>  **注意**：因为我不会封装之类的技术，您需要直接下载源代码，加载在您的工作环境内，再创建一个文件调用这些程序  

PCE Runs on Java, Recommend Java 11+.  
System Requirement: [Apfloat for Java](http://www.apfloat.org/apfloat_java/)  
  
PCE需要 Java 运行，推荐 Java  11+  
需要外部库运行：[Apfloat for Java](http://www.apfloat.org/apfloat_java/)  
  
## Using PCE / 使用方式  

### Brief Introduction:  
Create a new file after set up the working environment, for example ```output.java```.  
* In this file, you could use  
  ```MCbeDrawLines.MethodIdentifier(<String: line name>, <double: x1>, <double: x2>, <double: y1>, <double: y2>, <double: height>, <String: particle_name>, <int: spiral count>);```  
  to draw a line between ```(x1, height, y1) ``` to ```(x2, height, y2)``` (Minecaft Coordinate)
* You could also use  
  ```BlockFloat.fade(<double: x>, <double: y>, <double: height>, <String: effect_type>);```  
  to use the floating effect.
 * And you could use 
	```BlockToRipple.ripple(<double: x>, <double: y>, <double: height>, <String: effect_type>, <String: particle_name>);```  
	to change blocks into moving particles.  
  
### 简介：  
在将Apfloat和程序正确的配置到工作环境之后，创建一个新的文件，比如 ```output.java```  
* 在这个文件中，你可以使用  
  ```MCbeDrawLines.MethodIdentifier(<String: 线种类>, <double: x1>, <double: x2>, <double: y1>, <double: y2>, <double: 高度>, <String: 粒子名称>, <int: 螺旋数量>);```  
  链接 ```(x1, height, y1) ``` 到 ```(x2, height, y2)``` (Minecaft 坐标)  
 * 你还可以使用  
   ```BlockFloat.fade(<double: x>, <double: y>, <double: 高度>, <String: 效果>);```    
   来使用漂浮消失特效
 * 或者使用  
   ```BlockToRipple.ripple(<double: x>, <double: y>, <double: 高度>, <String: 效果>, <String: 例子名称>);```  
   使用方块变涟漪特效  
  
## ```MCbeDrawLines.MethodIdentifier()```  
Using this method, you could use four different kinds of line or curve to connect 2 coordinates.  
* ```<String: line name>```
<div>
<table>  
<tr>  
<th>Parameter</th>  
<th>Description</th>  
</tr>  
<tr>  
<td><code>"line"</code></td>  
<td>Use A Segment Connect 2 Coordinates</td>  
</tr>  
<tr>  
<td><code>"parabola"</code></td>  
<td>Use A Part of Parabola Connect 2 Coordinates</td>  
</tr>  
<tr>  
<td><code>"SpiralLine"</code></td>  
<td>Use Segment Connect 2 Coordinates, But Particles Will Move and Create Spiral Line Effect</td>  
</tr>  
</tr>  
<tr>  
<td><code>"SpiralParabola"</code></td>  
<td>Use A Part of Parabola Connect 2 Coordinates, But Particles Will Move and Create Spiral Line Effect</td>  
</tr>  
</table>
</div>

* Coordinates
<div>
<table>  
<tr>  
<th>Parameter</th>  
<th>Description</th>  
</tr>  
<tr>  
<td>ㅤㅤㅤ<code>&ltdouble: x1&gt</code></td>  
<td>ㅤㅤㅤx coordinate of first block</td>  
</tr>  
<tr>  
<td>ㅤㅤㅤ<code>&ltdouble: x2&gt</code></td>  
<td>ㅤㅤㅤx coordinate of second block</td>  
</tr>  
<tr>  
<td>ㅤㅤㅤ<code>&ltdouble: y1&gt</code></td>  
<td>ㅤㅤㅤz coordinate of first block</td>  
</tr>  
</tr>  
<tr>  
<td>ㅤㅤㅤ<code>&ltdouble:y2&gt</code>ㅤ</td>  
<td>ㅤㅤㅤz coordinate of second block</td>  
</tr>  
<tr>  
<td>ㅤㅤㅤ<code>&ltdouble:height&gt</code>ㅤㅤㅤ</td>  
<td>ㅤㅤㅤy coordinate of both blocksㅤㅤㅤ</td>  
</tr>  
</table>
</div>

>**NOTE**: All coordinate in description follows Minecraft coordinate system. In Minecraft, x and z values represent East, West, North, and South, y value represents the height; where normally, z value represents the height.  

* ```<String: particle_name>```  
This String input means what particle PCE is going to use. The output would be:  
```particle minecraft:```**```particle_name```**``` x y z```
<div>
<table>  
<tr>  
<th>Particle Name</th>  
<th>Description</th>  
<th>GIF</th>
</tr>  
<tr>  
<td><code>"endRod"</code></td>  
<td>A colorful particle included in PCE, modified from <code>"endrod"</code></td>
<td><img src=https://raw.githubusercontent.com/FedDragon1/PEC/main/logo/endRod.gif></td>  
</tr>  
<tr>  
<td><code>"gradient"</code></td>  
<td>Particles which change form purple to white / white to purple</td>  
<td><img src=https://raw.githubusercontent.com/FedDragon1/PEC/main/logo/gradient.gif></td>  
</tr>  
<tr>  
<td><code>"endrod_no_gravity"</code></td>  
<td>Modified <code>endrod</code> which removes gravity and changed texture.</td>  
<td><img src=https://raw.githubusercontent.com/FedDragon1/PEC/main/logo/endrod_ng.gif></td>  
</tr>  
</tr>  
<tr>  
<td>...</td>  
<td>...</td>  
<td>...</td>  
</tr>  
</table>
</div>

* ```<int: count>```  
 Only used in ```SpiralLine``` and ```SpiralParabola```.  
<div>
<table>  
<tr>  
<th>Count</th>  
<th>Line Type</th>
<th>Preview</th>  
</tr>  
<tr>  
<td><code>default</code></td>  
<td rowspan="2">Spiral Line</td>  
<td><img src=https://raw.githubusercontent.com/FedDragon1/PEC/main/logo/spiral_line_1.gif></td>  
</tr>  
<tr>  
<td>ㅤ<code>2</code>ㅤ</td>  
<td><img src=https://raw.githubusercontent.com/FedDragon1/PEC/main/logo/spiral_line_2.gif></td>  
</tr>   
<tr>  
<td><code>default</code></td>  
<td rowspan="2">Spiral Parabola</td>  
<td><img src=https://raw.githubusercontent.com/FedDragon1/PEC/main/logo/spiral_parabola_1.gif></td>  
</tr>  
<tr>  
<td>ㅤ<code>2</code>ㅤ</td>  
<td><img src=https://raw.githubusercontent.com/FedDragon1/PEC/main/logo/spiral_parabola_2.gif></td>  
</tr>  
</table>
</div>

---
利用这个方法，您可以用4种样式的线连接两个坐标
* ```<String: 线种类>```
<div>
<table>  
<tr>  
<th>参数</th>  
<th>实际效果</th>  
</tr>  
<tr>  
<td><code>"line"</code></td>  
<td>用线段连接两个坐标</td>  
</tr>  
<tr>  
<td><code>"parabola"</code></td>  
<td>用抛物线连接两个坐标</td>  
</tr>  
<tr>  
<td><code>"SpiralLine"</code></td>  
<td>用线段连接两个坐标，不过粒子会移动形成螺旋线</td>  
</tr>  
</tr>  
<tr>  
<td><code>"SpiralParabola"</code></td>  
<td>用抛物线连接两个坐标，不过粒子会移动形成螺旋抛物线</td>  
</tr>  
</table>
</div>

* Coordinates
<div>
<table>  
<tr>  
<th>参数</th>  
<th>意义</th>  
</tr>  
<tr>  
<td>ㅤㅤㅤ<code>&ltdouble: x1&gt</code></td>  
<td>ㅤㅤㅤ第一个方块的 x 坐标</td>  
</tr>  
<tr>  
<td>ㅤㅤㅤ<code>&ltdouble: x2&gt</code></td>  
<td>ㅤㅤㅤ第二个方块的 x 坐标</td>  
</tr>  
<tr>  
<td>ㅤㅤㅤ<code>&ltdouble: y1&gt</code></td>  
<td>ㅤㅤㅤ第一个方块的 z 坐标</td>  
</tr>  
</tr>  
<tr>  
<td>ㅤㅤㅤ<code>&ltdouble:y2&gt</code>ㅤ</td>  
<td>ㅤㅤㅤ第二个方块的 z 坐标</td>  
</tr>  
<tr>  
<td>ㅤㅤㅤ<code>&ltdouble:height&gt</code>ㅤㅤㅤ</td>  
<td>ㅤㅤㅤ两个方块相同的 y 坐标ㅤㅤㅤ</td>  
</tr>  
</table>
</div>

>**注意**: 上面使用的坐标均为MC坐标。在Minecraft，x 和 z 坐标确定东西南北，y 坐标确定高度；然而一般的空间直角坐标系中 z 轴确定高度。

* ```<String: 粒子名称>```  
This String input means what particle PCE is going to use. The output would be:  
```particle minecraft:```**```粒子名称```**``` x y z```
<div>
<table>  
<tr>  
<th>粒子名称</th>  
<th>描述</th>  
<th>GIF</th>
</tr>  
<tr>  
<td><code>"endRod"</code></td>  
<td>五彩斑斓的粒子，从 <code>"末地烛"</code> 的粒子修改而来</td>
<td><img src=https://raw.githubusercontent.com/FedDragon1/PEC/main/logo/endRod.gif></td>  
</tr>  
<tr>  
<td><code>"gradient"</code></td>  
<td>由多个粒子组成的“粒子群”，有些粒子从白色渐变到紫色，其他的从紫色渐变到白色。</td>  
<td><img src=https://raw.githubusercontent.com/FedDragon1/PEC/main/logo/gradient.gif></td>  
</tr>  
<tr>  
<td><code>"endrod_no_gravity"</code></td>  
<td>从 <code>endrod</code> 修改，移除了原版末地烛的向下位移并修改材质</td>  
<td><img src=https://raw.githubusercontent.com/FedDragon1/PEC/main/logo/endrod_ng.gif></td>  
</tr>  
</tr>  
<tr>  
<td>...</td>  
<td>...</td>  
<td>...</td>  
</tr>  
</table>
</div>

* ```<int: count>```  
 只在 ```SpiralLine``` 和 ```SpiralParabola``` 中使用。  
<div>
<table>  
<tr>  
<th>参数</th>  
<th>线名称</th>
<th>Preview</th>  
</tr>  
<tr>  
<td><code>其他</code></td>  
<td rowspan="2">Spiral Line</td>  
<td><img src=https://raw.githubusercontent.com/FedDragon1/PEC/main/logo/spiral_line_1.gif></td>  
</tr>  
<tr>  
<td>ㅤ<code>2</code>ㅤ</td>  
<td><img src=https://raw.githubusercontent.com/FedDragon1/PEC/main/logo/spiral_line_2.gif></td>  
</tr>   
<tr>  
<td><code>其他</code></td>  
<td rowspan="2">Spiral Parabola</td>  
<td><img src=https://raw.githubusercontent.com/FedDragon1/PEC/main/logo/spiral_parabola_1.gif></td>  
</tr>  
<tr>  
<td>ㅤ<code>2</code>ㅤ</td>  
<td><img src=https://raw.githubusercontent.com/FedDragon1/PEC/main/logo/spiral_parabola_2.gif></td>  
</tr>  
</table>
</div>
