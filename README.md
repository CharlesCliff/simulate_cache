# simulate_cache
Cacheģ��
һ������Ҫ��ʵ����
1��cache��ͬ���ȵĶ�д��������չ/�޷�����չ��д��
2��cache���滻linefill��cache missʱ���У�����ÿ����鶼�����ݣ���ô��
LRU�㷨�滻�������ʹ�õ�block
3��ģ������cache missʱ�Ĵ�memoryȡ������linefillʱ����
4������cache,Invalid Entire Cache
��������˵��
��������java������Ҫ��װjava֧�֣����巽�������jdk��װ˵��
�����Ǳ�������

1����window�����д���cmd
2������Ƿ���Java֧�� ��������java -version,����java �汾��Ϣ����У������밲װjdk����
2���л�Ŀ¼�����ļ�����Ŀ¼  cd /path/to/thisfile 
3��ִ������:
		cd simulate_cache
		javac -d bin/ src/simulate_cache/*.java
		cd bin/
		java simulate_cache/SimulateCache
		binĿ¼���б���õ�.class�ļ�����ֱ��ִ��
		
�������⣬����ϵ 1150026521@qq.com