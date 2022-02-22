package org.jeecg.modules.common;


import com.google.common.collect.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.formula.functions.T;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.*;

/**
 * @author zlf
 */
public  class IscTools {

    /**
     * List集合拼接成以逗号分隔的字符串
     * 可以用stream流 String join = list.stream().collect(Collectors.joining(","));
     */
    public String splitStringList(List<String> list){
        String collect = String.join(",", list);
        if (StringUtils.isNotEmpty(collect)){
            return collect;
        }else {
            return "";
        }
    }

    /**
     * 比较两个字符串是否相等，忽略大小写
     */
    public boolean equalsIgnoreCase(String strA,String strB) {
        return strA.equalsIgnoreCase(strB);
    }

    /**
     * 比较两个对象是否相等
     */
    public boolean equalsObject(Object a, Object b){
        return Objects.equals(a, b);
    }

    /**
     * 两个List集合取交集
     */
    public List<?>  intersectionList(List<?> listA,List<?> listB){
        listA.retainAll(listB);
        return listA;
    }

    /**
     * 两个集合取交集
     * @param listA
     * @param listB
     * @return
     */
    public Collection<?>  retainAll(List<?> listA,List<?> listB){
        return CollectionUtils.retainAll(listA, listB);
    }

    /**
     * 两个集合取并集
     * @param listA
     * @param listB
     * @return
     */
    public Collection<?>  union(List<?> listA,List<?> listB){
        return CollectionUtils.union(listA, listB);
    }

    /**
     * 两个集合取差集
     * @param listA
     * @param listB
     * @return
     */
    public Collection<?>  subtract(List<?> listA,List<?> listB){
        return CollectionUtils.subtract(listA, listB);
    }

    /**
     * 判断集合是否为空
     * @param coll
     * @return
     */
    public static boolean isCollection(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }
    /**
     * 判断数组是否为空
     * @param array
     * @return
     */
    public static boolean isEmptyArray(Object[] array) {
        return array == null || array.length==0;
    }

    /**
     * 集合不为空
     * @param coll
     * @return
     */
    public static boolean isNotCollection(Collection<?> coll) {
        return !isCollection(coll);
    }

    /**
     * 首字母转成大写
     * @param data 数据
     * @return
     */
    public static String initialToCapital(String data){
        if (StringUtils.isNotEmpty(data)){
            return  StringUtils.capitalize(data);
        }else {
            return "";
        }
    }

    /**
     * 重复拼接字符串
     * @param data 字符串
     * @param count 拼接次数
     * @return
     */
    public static String repeatStitchingString(String data,int count){
        return StringUtils.repeat("ab", count);
    }

    /**
     * 格式化日期
     * @param date 时间
     * @return
     */
    public static String formatDate(Date date){
        return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
    }


    /**
     *格式化日期
     * @param date 时间
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date) throws ParseException {
        return DateUtils.parseDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /***
     * 计算几个个小时后的日期
     * @param hour 小时
     * @return
     */
    public static Date calculationDate(int hour){
        return DateUtils.addHours(new Date(), hour);
    }

    /**
     * 设置对象属性
     * @param object 对象
     * @param property 属性
     * @param value 值
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static T setProperties(T object, String property, T value) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.setProperty(object, property, value);
        return object;
    }

    /**
     *对象转map
     * @param object 对象
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public Map<?,?> objectToMap(T object) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<?, ?> map = BeanUtils.describe(object);
        return map;
    }

    /**
     *map转对象
     * @param object 对象
     * @param map map集合
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public T mapToObject(T object,Map<String,?> map) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        BeanUtils.populate(object, map);
        return object;
    }

    /**
     * 读取文件
     * @param pathName 文件路径
     * @return
     * @throws IOException
     */
    public List<String> readLines(String pathName) throws IOException {
        File file = new File(pathName);
        return FileUtils.readLines(file, Charset.defaultCharset());
    }

    /**
     * 写入文件
     * @param pathName 文件路径
     * @param lines 写入内容
     * @throws IOException
     */
    public void writeLines(String pathName,List<String> lines) throws IOException {
        FileUtils.writeLines(new File(pathName), lines);
    }

    /**
     * 复制文件
     * @param srcFile srcFile
     * @param destFile destFile
     * @throws IOException
     */
    public void copyFile(File srcFile, File destFile) throws IOException {
        FileUtils.copyFile(srcFile, destFile);
    }

    /**
     *反转list
     * @param list list集合
     * @return
     */
    public List<?>  reverse(List<?> list){
        return Lists.reverse(list);
    }

    /**
     * list集合元素太多，可以分成若干个集合，每个集合N个元素
     * @param list
     * @param count
     * @return
     */
    public List<? extends List<?>>  partition(List<?> list,int count){
        return Lists.partition(list, count);
    }

    /**
     * Multimap 一个key可以映射多个value的HashMap
     */
    public void multimap(){
        Multimap<String, Integer> map = ArrayListMultimap.create();
        map.put("key", 1);
        map.put("key", 2);
        Collection<Integer> values = map.get("key");
        // 输出 {"key":[1,2]}
        System.out.println(map);
        // 还能返回你以前使用的臃肿的Map
        Map<String, Collection<Integer>> collectionMap = map.asMap();
    }

    /**
     * BiMap 一种连value也不能重复的HashMap
     */
    public void biMap(){
        BiMap<String, String> biMap = HashBiMap.create();
        // 如果value重复，put方法会抛异常，除非用forcePut方法
        biMap.put("key","value");
        // 输出 {"key":"value"}
        System.out.println(biMap);
        // 既然value不能重复，何不实现个翻转key/value的方法，已经有了
        BiMap<String, String> inverse = biMap.inverse();
        // 输出 {"value":"key"}
        System.out.println(inverse);
    }

    /**
     * Table 一种有两个key的HashMap
     */
    public void twoKeyTable(){
        // 一批用户，同时按年龄和性别分组
        Table<Integer, String, String> table = HashBasedTable.create();
        table.put(18, "男", "yideng");
        table.put(18, "女", "Lily");
        // 输出 yideng
        System.out.println(table.get(18, "男"));
        // 这其实是一个二维的Map，可以查看行数据
        Map<String, String> row = table.row(18);
        // 输出 {"男":"yideng","女":"Lily"}
        System.out.println(row);
        // 查看列数据
        Map<Integer, String> column = table.column("男");
        // 输出 {18:"yideng"}
        System.out.println(column);
    }

    /**
     * Multiset 一种用来计数的Set
     */
    public void multisetCount(){
        Multiset<String> multiset = HashMultiset.create();
        multiset.add("apple");
        multiset.add("apple");
        multiset.add("orange");
        // 输出 2
        System.out.println(multiset.count("apple"));
        // 查看去重的元素
        Set<String> set = multiset.elementSet();
        // 输出 ["orange","apple"]
        System.out.println(set);
        // 还能查看没有去重的元素
        for (String s : multiset) {
            System.out.println(s);
        }
        // 还能手动设置某个元素出现的次数
        multiset.setCount("apple", 5);
    }

    /**
     * 判断操作系统是否是Windows
     *
     * @return
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

}
