package com.nms.util.test;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class Java8DemoTest {

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertEquals(2, a);
    }

    /**
     * 接口默认方法
     * Java 8 使我们能够使用default 关键字给接口增加非抽象的方法实现。这个特性也被叫做 扩展方法（Extension Methods）。
     */
    @Test
    public void testInterfaceDefaultMethod()
    {
        Car car = new Car() {
            @Override
            public CarBody body(LevelType levelType) {
                return new CarBody(4860,1950,1760,2700);
            }
        };
        Car.CarBody body = car.body(Car.LevelType.SUV);
        System.out.println("长="+ body.getLength() +"宽="+ body.getWidth() +"高="+ body.getHeight() +"轴距=" + body.getWheelbase());
        //di可以开箱即用
        System.out.println(car.di());
    }

    /**
     * 函数式接口
     * 每个lambda对应于一个给定的类型，用一个接口来说明。而这个被称为函数式接口（functional interface）的接口必须仅仅包含一个抽象方法声明。
     * 每个那个类型的lambda表达式都将会被匹配到这个抽象方法上。因此默认的方法并不是抽象的，你可以给你的函数式接口自由地增加默认的方法。
     *
     * 我们可以使用任意的接口作为lambda表达式，只要这个接口只包含一个抽象方法。为了保证你的接口满足需求，你需要增加@FunctionalInterface注解。
     * 编译器知道这个注解，一旦你试图给这个接口增加第二个抽象方法声明时，它将抛出一个编译器错误。     *
     */
    @Test
    public void testFunctionalInterface()
    {
        Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
        Integer converted = converter.convert("123");
        System.out.println(converted);    // 123
    }

    /**
     * 内置功能接口
     * 谓词：谓词是单参数的布尔值函数。该接口包含多个默认的方法使谓词转换成复杂的逻辑表达式（与，或，非）。
     * 函数：函数接受单一参数，产出结果。默认的方法可被用来将多个函数链接起来（compose，andThen）。
     * 生产者：生产者产生一个给定的泛型类型的结果。区别于函数，生产者不接受参数。
     * 消费者：消费者代表了将要对一个单一输入参数采取的运算。
     * 比较器：比较器在较老的 Java版本中众所周知。Java 8给这个接口增加了多个默认的方法。
     * Optionals：Optionals并不是函数式接口，而是一个避免空指针异常NullPointerException的俏皮工具。
     * Streams：java.util.Stream代表元素的一个序列，一个或者多个运算可以在这个序列上运行。
     *          Stream运算可以是中间的（intermediate），也可是末端的（terminal）。
     *          末端运算返回具有特定类型的结果，中间运算返回 stream 自身，所以可以将多个方法调用串联在一行。
     *          Streams是在一个源上创建的，例如，一个java.util.Collection 类似的lists或者sets（不支持maps）。
     *          Sream运算可以被顺序执行或者并行执行。
     *      Filter：Filter接受一个谓词来过滤出流中所有的元素。此运算是一个中间运算，它可以使我们在结果上调用其它的stream运算（forEach）。
     *        forEach 接受一个可以对每个流过滤出的元素进行操作的消费者。forEach 是一个末端运算，换句话说，我们不能再调用其他的流运算。
     *      Sorted：Sorted是一个返回流的排序视图的中间运算。除非你传递一个定制的Comparator，元素将被以自然顺序进行排序。
     *        Sorted真的仅仅对此stream创建一个排序的视图，它并不操纵背后的聚集（collection）。
     *      Map：中间运算 map 将每个元素通过给定的函数转变为其它对象。
     *      匹配：多个匹配运算可以被用来检验是否一个特定的谓词与某stream匹配。所有的这些运算都为末端运算，并且返回一个布尔值结果。
     *      计数：计数是一个末端运算，以long类型返回在stream中的元素的数目。
     *      Reduce：这个末端运算使用给定的函数对stream的元素进行一个聚合（减缩）运算。结果是一个保存有聚合（减缩）值的Optional。
     *      Parallel Streams：streams可以是顺序或者并行的。在顺序streams上的操作是在一个单线程中完成的，然而在并行streams上的操作时在多个线程上并发完成的。
     * Map：Map并不支持streams。然而，Map现在支持多种新的有用的方法来完成普通的任务。
     */
    @Test
    public void testInnerFunctionalInterface()
    {
        //谓词
        Predicate predicate = (s) -> ((String)s).length() > 0;

        predicate.test("foo");              // true
        predicate.negate().test("foo");     // false

        Predicate nonNull = Objects::nonNull;
        Predicate isNull = Objects::isNull;

//        Predicate isEmpty = String::isEmpty;
//        Predicate isNotEmpty = isEmpty.negate();
    }

    /**
     * Date API
     *
     */




    /**
     * lambda维基百科的定义： a function (or a subroutine) defined, and possibly called, without being bound to an identifier。
     *                     一个不用被绑定到一个标识符上，并且可能被调用的函数。
     *
     * 简单理解：            一段有输入参数的可执行语句块。
     *
     * lambda一般语法
     * (Type1 param1, Type2 param2, ..., TypeN paramN) -> {
     *      statment1;
     *      statment2;
     *      //.............
     *      return statmentM;
     * }
     * 1.通常情况参数类型类型推断
     * (param1, param2, ..., paramN) -> {
     *      statment1;
     *      statment2;
     *      //.............
     *      return statmentM;
     * }
     * 2.当lambad表达式参数只有一个时，可以省略小括号
     * param1 -> {
     *     statment1;
     *     statment2;
     *     //...
     *     return statmentM;
     * }
     * 3.当lambad表达式只包含一条语句时，可以省略大括号，return和语句结尾的分号
     * param1 -> statment
     * 4.使用Method Reference
     *
     */
    @Test
    public void testLambda()
    {
        List<String> names = new ArrayList<>();
        names.add("Sam");
        names.add("Tom");
        names.add("Jack");
        //只有一行代码可以直接写并作为返回值
        Collections.sort(names,(String n1,String n2) -> n1.compareTo(n2));
        System.out.println(names);

        names.clear();
        names.add("Sam");
        names.add("Tom");
        names.add("Jack");

        //类型推断
        Collections.sort(names,(n1,n2) -> n1.compareTo(n2));
        System.out.println(names);

        names.clear();
        names.add("Sam");
        names.add("Tom");
        names.add("Jack");

        //流式编程,类型推断
        List<String> upperNames = names
                .stream()
                .map((name)->name.toUpperCase())
                .filter(name->name.startsWith("S"))
                .collect(Collectors.toList());

        System.out.println(upperNames);

        //Method Refrence
        List<String> upperNames1 = names
                .stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        System.out.println(upperNames1);

        /**
         * 不过lambda表达式访问外部变量有一个非常重要的限制：变量不可变（只是引用不可变，而不是真正的不可变）
         */
        String[] array = {"a", "b", "c"};
        for(Integer i : Lists.newArrayList(1,2,3)){
            Stream.of(array)
                    .map(item -> Strings.padEnd(item, i, '@'))
                    .forEach(System.out::println);
        }

        //此处i会变，不能通过编译
//        String[] array = {"a", "b", "c"};
//        for(int i = 1; i<4; i++){
//            Stream.of(array).map(item -> Strings.padEnd(item, i, '@')).forEach(System.out::println);
//        }

        /**
         * 在lambda中，this不是指向lambda表达式产生的那个SAM对象，而是声明它的外部对象。
         */

        names.clear();
        names.add("Sam");
        names.add("Tom");
        names.add("Jack");

        Person p = new Person();
        p.friends(names);

        /**
         * 方法引用（Method reference）和构造器引用(construct reference)
         *
         * 方法引用可以在某些条件成立的情况下，更加简化lambda表达式的声明。方法引用语法格式有以下三种：
         * objectName::instanceMethod
         * ClassName::staticMethod
         * ClassName::instanceMethod
         * 前两种方式类似，等同于把lambda表达式的参数直接当成instanceMethod|staticMethod的参数来调用。
         * 比如System.out::println等同于x->System.out.println(x)；Math::max等同于(x, y)->Math.max(x,y)。
         * 最后一种方式，等同于把lambda表达式的第一个参数当成instanceMethod的目标对象，其他剩余参数当成该方法的参数。
         * 比如String::toLowerCase等同于x->x.toLowerCase()。
         *
         * 构造器引用
         * 构造器引用语法如下：ClassName::new，把lambda表达式的参数当成ClassName构造器的参数 。
         * 例如BigDecimal::new等同于x->new BigDecimal(x)。
         */
    }

    /**
     * Stream
     * A sequence of elements supporting sequential and parallel aggregate operations.
     * 1.Stream是元素的集合，这点让Stream看起来用些类似Iterator；
     * 2.可以支持顺序和并行的对原Stream进行汇聚的操作；
     * Stream的基本使用步骤：
     * 1.创建Stream
     * 1.1 通过Stream接口静态工厂方法。（注意：Java8新特性，接口可以带静态方法）
     * 1.2 通过Collection接口默认方法（默认方法：Default method，也是Java8中的一个新特性，就是接口中的一个带有实现的方法，后续文章会有介绍）–stream()，把一个Collection对象转换成Stream
     * 2.转换Stream,每次转换原有Stream对象不改变，返回一个新的Stream对象。
     * 3.对Stream进行Reduce(聚合)操作，获取想要的结果。
     *
     */
    @Test
    public void testStream()
    {
        List<String> names = new ArrayList<>();
        names.add("Sam");
        names.add("");
        names.add("Tom");
        names.add("");
        names.add("Jack");
        names.add("");

        System.out.println("not empty name count=" + names
                .stream()
                .filter(name->!name.isEmpty())
                .count());


        /**
         * 1.使用Stream静态方法创建Stream         *
         */
        //1.1 of方法，有两个overload方法，一个接受变长参数，一个接受单一值
        Stream<Integer> integerStream = Stream.of(1, 2, 3, 5);
        Stream<String> stringStream = Stream.of("taobao");
        //1.2 generator方法：生成一个无限长度的Stream，其元素的生成是通过给定的Supplier
        // （这个接口可以看成一个对象的工厂，每次调用返回一个给定类型的对象）
        //  一般这种无限长度的Stream都会配合Stream的limit()方法来用
        Stream.generate(new Supplier<Double>() {
            @Override
            public Double get() {
                return Math.random();
            }
        });
        Stream.generate(() -> Math.random());
        Stream.generate(Math::random);

        //1.3 iterate方法：也是生成无限长度的Stream，和generator不同的是，其元素的生成是重复对给定的种子值(seed)调用用户指定函数来生成的。
        // 其中包含的元素可以认为是：seed，f(seed),f(f(seed))无限循环，一般这种无限长度的Stream都会配合Stream的limit()方法来用
        Stream.iterate(1, item -> item + 1).limit(10).forEach(System.out::println);

        /**
         * 2.通过Collection子类获取Stream,本例的names就是一个例子。
         */
        names.stream().forEach(System.out::println);


        /**
         * 3.转换Stream
         * 转换Stream其实就是把一个Stream通过某些行为转换成一个新的Stream。Stream接口中定义了几个常用的转换方法,常用的包括：
         * 1. distinct: 对于Stream中包含的元素进行去重操作（去重逻辑依赖元素的equals方法），新生成的Stream中没有重复的元素；
         * 2. filter: 对于Stream中包含的元素使用给定的过滤函数进行过滤操作，新生成的Stream只包含符合条件的元素；
         * 3. map: 对于Stream中包含的元素使用给定的转换函数进行转换操作，新生成的Stream只包含转换生成的元素。
         *      这个方法有三个对于原始类型的变种方法，分别是：mapToInt，mapToLong和mapToDouble。
         *      这三个方法也比较好理解，比如mapToInt就是把原始Stream转换成一个新的Stream，这个新生成的Stream中的元素都是int类型。
         *      之所以会有这样三个变种方法，可以免除自动装箱/拆箱的额外消耗；
         * 4. flatMap：和map类似，不同的是其每个元素转换得到的是Stream对象，会把子Stream中的元素压缩到父集合中；
         * 5. peek: 生成一个包含原Stream的所有元素的新Stream，同时会提供一个消费函数（Consumer实例），新Stream每个元素被消费的时候都会执行给定的消费函数；
         * 6. limit: 对一个Stream进行截断操作，获取其前N个元素，如果原Stream中包含的元素个数小于N，那就获取其所有的元素；
         * 7. skip: 返回一个丢弃原Stream的前N个元素后剩下元素组成的新Stream，如果原Stream中包含的元素个数小于N，那么返回空Stream；
         */
        List<Integer> nums = new ArrayList<>();
        nums.add(1);
        nums.add(null);
        nums.add(2);
        nums.add(null);
        nums.add(3);
        nums.add(4);
        nums.add(null);
        nums.add(5);
        nums.add(6);
        nums.add(7);
        nums.add(8);
        nums.add(9);
        nums.add(10);

        //转换操作都是lazy的，多个转换操作只会在Reduce(聚合)操作的时候融合起来，一次循环完成。
        int sumResult = nums
                .stream()
                .filter(n->n!=null)
                .distinct()
                .mapToInt(n->n*2)
                .peek(System.out::println)
                .skip(2)
                .limit(4)
                .sum();
        System.out.println("sum=" + sumResult);

        /**
         * 4.Reduce
         * Java doc中对于其定义：
         * A reduction operation (also called a fold) takes a sequence of input elements and combines them into a single summary result by repeated application of a combining operation,
         * such as finding the sum or maximum of a set of numbers,
         * or accumulating elements into a list.
         * The streams classes have multiple forms of general reduction operations,
         * called reduce() and collect(), as well as multiple specialized reduction forms such as sum(),
         * max(), or count().
         *
         * 聚合操作（也称为折叠）接受一个元素序列为输入，反复使用某个合并操作，把序列中的元素合并成一个汇总的结果。
         * 比如查找一个数字列表的总和或者最大值，或者把这些数字累积成一个List对象。
         * Stream接口有一些通用的汇聚操作，比如reduce()和collect()；
         * 也有一些特定用途的汇聚操作，比如sum(),max()和count()。
         * 注意：sum方法不是所有的Stream对象都有的，只有IntStream、LongStream和DoubleStream是实例才有。
         *
         * 聚合操作分为两类：
         * 1.可变聚合：把输入的元素们累积到一个可变的容器中，比如Collection或者StringBuilder；
         * 2.其他汇聚：除去可变汇聚剩下的，一般都不是通过反复修改某个可变对象，而是通过把前一次的汇聚结果当成下一次的入参，反复如此。比如reduce，count，allMatch；
         */
        //4.1 可变聚合
        // 可变汇聚对应的只有一个方法：collect，正如其名字显示的，它可以把Stream中的要有元素收集到一个结果容器中（比如Collection）。
        // 先看一下最通用的collect方法的定义（还有其他override方法）
        // <R> R collect(Supplier<R> supplier,
        //                  BiConsumer<R, ? super T> accumulator,
        //                  BiConsumer<R, R> combiner);
        // 参数的含义：
        // Supplier supplier是一个工厂函数，用来生成一个新的容器；
        // BiConsumer accumulator也是一个函数，用来把Stream中的元素添加到结果容器中；
        // BiConsumer combiner还是一个函数，用来把中间状态的多个结果容器合并成为一个（并发的时候会用到）。
        List<Integer> numsWithoutNull = nums
                .stream()
                .filter(num -> num != null)
                .collect(() -> new ArrayList<Integer>(),
                        (list, item) -> list.add(item),
                        (list1, list2) -> list1.addAll(list2));

        //代码解析：
        //第一个函数生成一个新的ArrayList实例；
        //第二个函数接受两个参数，第一个是前面生成的ArrayList对象，二个是stream中包含的元素，函数体就是把stream中的元素加入ArrayList对象中。第二个函数被反复调用直到原stream的元素被消费完毕；
        //第三个函数也是接受两个参数，这两个都是ArrayList类型的，函数体就是把第二个ArrayList全部加入到第一个中；

        //Collectors工具类简化开发
        List<Integer> numsWithoutNull1 = nums
                .stream()
                .filter(num -> num != null)
                .collect(Collectors.toList());
        //4.2 其他聚合
        // 4.2.1 reduce方法：
        //       reduce方法非常的通用，后面介绍的count，sum等都可以使用其实现。
        //       reduce方法有三个override的方法，其中两个最常用的，先来看reduce方法的第一种形式，其方法定义如下：
        //       Optional<T> reduce(BinaryOperator<T> accumulator);
        //       接受一个BinaryOperator类型的参数，在使用的时候我们可以用lambda表达式来。
        System.out.println("sum is:" + nums
                .stream()
                .filter(num -> num != null)
                .reduce((sum, item) -> sum + item)
                .get());
        //代码解析：
        //可以看到reduce方法接受一个函数，这个函数有两个参数，第一个参数是上次函数执行的返回值（也称为中间结果），
        // 第二个参数是stream中的元素，这个函数把这两个值相加，得到的和会被赋值给下次执行这个函数的第一个参数。
        // 注意：**第一次执行的时候第一个参数的值是Stream的第一个元素，第二个参数是Stream的第二个元素**。
        // 这个方法返回值类型是Optional，这是Java8防止出现空指针异常的一种可行方法

        //4.2.2 reduce多态方法
        //      T reduce(T identity, BinaryOperator<T> accumulator);
        //      这个定义上上面已经介绍过的基本一致，不同的是：它允许用户提供一个循环计算的初始值，如果Stream为空，就直接返回该值。
        //      而且这个方法不会返回Optional，因为其不会出现null值。
        System.out.println("sum is:" + nums
                .stream()
                .filter(num -> num != null)
                .reduce(0,(sum, item) -> sum + item));

        //4.2.2 count方法:
        //      获取Stream中元素的个数。
        System.out.println("notNull count is:" + nums
                .stream()
                .filter(num -> num != null)
                .count());

        //4.2.3 搜索相关
        //  allMatch：是不是Stream中的所有元素都满足给定的匹配条件
        //  anyMatch：Stream中是否存在任何一个元素满足匹配条件
        //  findFirst: 返回Stream中的第一个元素，如果Stream为空，返回空Optional
        //  noneMatch：是不是Stream中的所有元素都不满足给定的匹配条件
        //  max和min：使用给定的比较器（Operator），返回Stream中的最大|最小值

        System.out.println("less 100 is:" + nums
                .stream()
                .filter(num -> num != null)
                .allMatch(item-> item < 100));

        nums
                .stream()
                .filter(num -> num != null)
                .max((n1,n2)->n1.compareTo(n2))
                .ifPresent(System.out::println);
    }





    static class Person
    {
        private String name = "Anny";

        public void friends(List<String> friends)
        {
            friends
                    .stream()
                    .map((name) -> this.name + " and "+ name +" is firend")
                    .collect(Collectors.toList())
                    .forEach(System.out::println);
        }
    }
}
