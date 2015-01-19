# simulate_cache
Cache模拟
一、根据要求，实现了
1、cache不同粒度的读写，符号扩展/无符号扩展读写，
2、cache行替换linefill在cache miss时进行，如果该块两组都有数据，那么用
LRU算法替换最近最少使用的block
3、模拟了在cache miss时的从memory取数，在linefill时发生
4、重置cache,Invalid Entire Cache
二、运行说明
本程序是java程序，需要安装java支持，具体方法请参照jdk安装说明
下面是编译运行

1、打开window命令行窗口cmd
2、检查是否有Java支持 输入命令java -version,如有java 版本信息则可行，否则请安装jdk环境
2、切换目录到本文件所在目录  cd /path/to/thisfile 
3、执行命令:
		cd simulate_cache
		javac -d bin/ src/simulate_cache/*.java
		cd bin/
		java simulate_cache/SimulateCache
		bin目录下有编译好的.class文件，可直接执行
		
如有问题，请联系 1150026521@qq.com