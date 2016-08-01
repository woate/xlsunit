package com.coamctech.xlsunit;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *  找到符合自己需要的类，主要是Entity
 * @author lijiazhi
 *
 */
public class ClassScaner {
	static boolean win = true ;
	static{
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name").toLowerCase();
		System.out.println(os);
		win = os.startsWith("win");
		
	}
	
    List<ClassScanerFilter> filters = new ArrayList<ClassScanerFilter>();
    String rootPkg;

    public ClassScaner(String pkg, ClassScanerFilter... filters0) {
        super();
        for (ClassScanerFilter filter : filters0) {
            this.filters.add(filter);
        }
        this.rootPkg = pkg;
    }

    /**
     * 获取项目的path下所有的文件夹和文件
     *
     * @return 文件列表
     */
    List<File> listPaths() {
        List<File> files = new ArrayList<File>();
        String jars = System.getProperty("java.class.path");
        if (jars == null) {
            System.err.println("java.class.path is null!");
            return files;
        }
        URL root = Thread.currentThread().getContextClassLoader()
                .getResource("");
        if (root == null) {
            System.err.println("path root is null!");
            return files;
        }
        String path = null;
        try {
            path = URLDecoder.decode(root.getFile(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return files;
        }
        File dir = new File(path);
        String[] array = (jars).split(win?";":":");
        if (array != null) {
            for (String s : array) {
                if (s == null) {
                    continue;
                }
                if (s.startsWith("java")) {
                    continue;
                }
                File f = new File(s);
                if (f.exists()) {
                    files.add(f);
                } else {// 有些jar就在系统目录下,省略了路径,要加上
                    File jar = new File(dir, s);
                    if (jar.exists()) {
                        files.add(jar);
                    }
                }
            }
        }
        return files;
    }

    /**
     * 获取包下所有的函数实现类
     * 包名,此处只是为了限定,防止漫无目的的查找.不用设置也可以,就要每找到一个类就要加载一次判断了
     *
     * @return 类列表
     */
    public List<Class<?>> getClasses() {
        Set<Class<?>> list = new HashSet<Class<?>>();

        for (File f : listPaths()) {
            // 如果是以文件的形式保存在服务器上
            if (f.isDirectory()) {
                // 获取包的物理路径
                String path = rootPkg.replace('.', File.separatorChar);
                dirWalker(path, f, list);
            } else {// 尝试是否是jar文件
                // 获取jar
                JarFile jar = null;
                try {
                    jar = new JarFile(f);
                } catch (IOException e) {
                    // 有可能不是一个jar
                }
                if (jar == null) {
                    continue;
                }
                String path = rootPkg.replace('.', '/');
                // 从此jar包 得到一个枚举类
                Enumeration<JarEntry> entries = jar.entries();
                // 同样的进行循环迭代
                while (entries.hasMoreElements()) {
                    // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    // 如果是以/开头的
                    if (name.charAt(0) == '/') {
                        // 获取后面的字符串
                        name = name.substring(1);
                    }
                    // 如果前半部分和定义的包名相同
                    if (name.contains(path)) {
                        if (name.endsWith(".class") && !entry.isDirectory()) {
                            name = name.replace("/", ".").substring(0,
                                    name.lastIndexOf("."));
                            try {
                                Class<?> c = Class.forName(name);
                                boolean fit = true;
                                for (ClassScanerFilter filter : this.filters) {
                                    if (!filter.accept(c)) {
                                        fit = false;
                                        break;
                                    }
                                }
                                if (fit) {
                                    list.add(c);
                                }
                            } catch (Throwable e) {
                                // 找不到无所谓了
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<Class<?>>(list);
    }

    /**
     * 遍历文件夹下所有的类
     *
     * @param path 包路径
     * @param file 文件
     * @param list 保存类列表
     */
    void dirWalker(String path, File file, Set<Class<?>> list) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    dirWalker(path, f, list);
                }
            } else {
                Class<?> c = loadClassByFile(path, file);
                if (c != null) {
                    list.add(c);
                }
            }
        }
    }

    /**
     * 从文件加载类
     *
     * @param pkg  包路径
     * @param file 文件
     * @return 类或者null
     */
    Class<?> loadClassByFile(String pkg, File file) {
        if (!file.isFile()) {
            return null;
        }
        String name = file.getName();
        if (name.endsWith(".class")) {
            String ap = file.getAbsolutePath();
            if (!ap.contains(pkg)) {
                return null;
            }
            name = ap.substring(ap.indexOf(pkg) + pkg.length());
            if (name.startsWith(File.separator)) {
                name = name.substring(1);
            }
            String path = (pkg + "." + name.substring(0, name.lastIndexOf(".")))
                    .replace(File.separatorChar, '.');
            try {
                Class<?> c = Class.forName(path);
                for (ClassScanerFilter filter : this.filters) {
                    if (!filter.accept(c)) {
                        return null;
                    }
                }
                return c;
            } catch (ClassNotFoundException e) {
                // do nothing
            }
        }
        return null;
    }
}
