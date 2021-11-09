

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
>  **提示**：因为我不会封装之类的技术，您需要直接下载源代码，加载在您的工作环境内，再创建一个文件调用这些程序  

PCE Runs on Java, Recommend Java 11+.  
System Requirement: [Apfloat for Java](http://www.apfloat.org/apfloat_java/)  
  
PCE需要 Java 运行，推荐 Java  11+ 
需要外部库运行：[Apfloat for Java](http://www.apfloat.org/apfloat_java/)  
  
## Using PCE / 使用方式  

Create a new file after set up the working environment, for example ```output.java```.  
* In this file, you could use  
  ```MCbeDrawLines.MethodIdentifier(<String: line name>, <double: x1>, <double: x2>, <double: y1>, <double: y2>, <double: height>, <String: particle>, <int: spiral count>);``` to draw a line between ```(x1, height, y1) ``` to ```(x2, height, y2)``` (Minecaft Coordinate)
* You could also use  
  ```BlockFloat.fade(<double: x>, <double: y>, <double: height>, <String: effect);```  
  to use the floating effect.
 * Or you could use 
	```a```
