package com.nms.util.test;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by sam on 17-6-1.
 */
public class FutureTest {
    private ExecutorService es;
    private Random rnd;
    @Before
    public void init()
    {
        es = Executors.newFixedThreadPool(10);
        rnd = new Random(System.currentTimeMillis());
    }

    /**
     * fake some business
     * @return
     */
    private Integer someBusiness()
    {
        long t = System.currentTimeMillis();
        System.out.println("begin compute...");
        int i = rnd.nextInt(10000);
        try{
            Thread.sleep(i);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("end compute. runtime= "+ (System.currentTimeMillis() - t)/1000+" seconds");
        return i;
    }

    @Test
    public void basic() throws ExecutionException,InterruptedException
    {
        ExecutorService es = Executors.newFixedThreadPool(1);
        Future<Integer> f = es.submit(() -> {
            //long compute
            return someBusiness();
        });
        // while(!f.isDone());
        int i = f.get();
        assertEquals(100,i);
    }


    /**
     * 查看抛出的异常的区别
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void joinVSget() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int i =1/0;
            return 100;
        });
//        future.join();
//        future.get();
    }

    public CompletableFuture<Integer> compute() {
        final CompletableFuture<Integer> future = new CompletableFuture<Integer>();
        return future;
    }

    /**
     * f.complete()或f.completeExceptionally()并没有在异步线程中执行的例子
     * @throws IOException
     */
    @Test
    public void thread() throws IOException {
        final CompletableFuture<Integer> f = compute();

        class Client extends Thread
        {
            CompletableFuture<Integer> f;
            Client(String threadName, CompletableFuture<Integer> f) {
                super(threadName);
                this.f = f;
            }
            @Override
            public void run() {
                try{
                    System.out.println(this.getName() +": "+ f.get());
                } catch(InterruptedException e) {
                    e.printStackTrace();
                } catch(ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        new Client("Client1", f).start();
        new Client("Client2", f).start();
        System.out.println("waiting");
        f.complete(100);
        //f.completeExceptionally(new Exception());
//        System.in.read();
    }


    @Test
    public void create()
    {
        CompletableFuture<Void> future10 = CompletableFuture.runAsync(() ->{
            //long compute
            someBusiness();
        });

        CompletableFuture<Void> future11 = CompletableFuture.runAsync(() ->{
            //long compute
            someBusiness();
        },es);

        CompletableFuture<Integer> future20 = CompletableFuture.supplyAsync(() -> {
            //long compute
            return someBusiness();
        });
        CompletableFuture<Integer> future21 = CompletableFuture.supplyAsync(() -> {
            //long compute
            return someBusiness();
        },es);
    }

    /**
     * 计算结果完成时的Action
     * 当 CompletableFuture的计算结果完成，或者抛出异常的时候，我们可以执行特定的 Action。
     * 方法不以Async结尾，意味着Action使用相同的线程执行，而Async可能会使用其它的线程去执行(如果使用相同的线程池，
     * 也可能会被同一个线程选中执行)。
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void action() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future21 = CompletableFuture.supplyAsync(() -> {
            //long compute
            //make exception
            int i = 1/0;
            return someBusiness();
        },es)
                .whenComplete((i,e) -> System.out.println("future complate result=" + i))
                .exceptionally((e) -> {
            System.out.println("future throw exception....");
//            System.out.println(e.getMessage());
            return 0;
        });

//        future21.whenCompleteAsync((i,e) -> System.out.println("future complate result=" + i));
//        future21.whenCompleteAsync((i,e) -> System.out.println("future complate result=" + i),es);

        int i = future21.get();
        System.out.println("result="+i);
        assertEquals(0,i);
    }

    /**
     * 转换
     * 回CompletableFuture对象，但是对象的值和原来的CompletableFuture计算的值不同。
     * 当原先的CompletableFuture的值计算完成或者抛出异常的时候，会触发这个CompletableFuture对象的计算，
     * 结果由 BiFunction 参数计算而得。因此这组方法兼有 whenComplete 和转换的两个功能。
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void handle() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future21 = CompletableFuture.supplyAsync(() -> {
            //long compute
            int i = someBusiness();
            System.out.println("business result="+i);
//            int x = 1/0;
            return i;
        },es).handle((i,e) -> {
            System.out.println("handle input i="+i);
            return i * 2;
        });

//        future21.handleAsync((i,e) -> System.out.println("future complate result=" + i));
//        future21.handleAsync((i,e) -> System.out.println("future complate result=" + i),es);

        int i = future21.get();
        System.out.println("result=" + i);
//        assertEquals(0,i);
    }

    /**
     * 转换
     * 当计算完成的时候请执行某个 function 。而且我们还可以将这些操作串联起来，或者将 CompletableFuture 组合起来。
     * 这一组函数的功能是当原来的CompletableFuture计算完后，将结果传递给函数fn，将fn的结果作为新的 CompletableFuture计算结果。
     * 因此它的功能相当于将CompletableFuture<T> 转换成 CompletableFuture<U>。
     * 注意，这些转换并不是马上执行的，也不会阻塞，而是在前一个stage完成后继续执行。
     * 它们与 handle 方法的区别在于 handle 方法会处理正常计算值和异常，因此它可以屏蔽异常，避免异常继续抛出。而 thenApply 方法只是用来处理正常值，因此一旦有异常就会抛出。
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void thenApply() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future21 = CompletableFuture.supplyAsync(() -> {
            //long compute
            int i = someBusiness();
            System.out.println("business result="+i);
            return i;
        },es).thenApply((i) -> {
            System.out.println("handle input i="+i);
            return i * 2;
        });

//        future21.thenApplyAsync((i) -> System.out.println("future complate result=" + i));
//        future21.thenApplyAsync((i) -> System.out.println("future complate result=" + i),es);

        int i = future21.get();
        System.out.println("result=" + i);
//        assertEquals(0,i);
    }

    /**
     * 纯消费
     * 只对结果执行 Action ,而不返回新的计算值
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void thenAccept() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future21 = CompletableFuture.supplyAsync(() -> {
            //long compute
            int i = someBusiness();
            System.out.println("business result="+i);
            return i;
        },es);

        future21.thenAccept((i) -> System.out.println("accept proccessing i="+i));

        int i = future21.get();
        System.out.println("result=" + i);
//        assertEquals(0,i);
    }

    /**
     * 纯消费
     * 当两个CompletionStage都正常完成计算的时候，就会执行提供的action
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void thenAcceptBoth() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future21 = CompletableFuture.supplyAsync(() -> {
            //long compute
            int i = someBusiness();
            System.out.println("business x result="+i);
            return i;
        },es);

        CompletableFuture<Integer> future22 = CompletableFuture.supplyAsync(() -> {
            //long compute
            int i = someBusiness();
            System.out.println("business y result="+i);
            return i;
        },es);

        future21.thenAcceptBoth(future22,(x,y) -> System.out.println("acceptBoth proccessing sum="+ (x + y)));


        int x = future21.get();
        int y =  future22.get();

        System.out.println("result x=" + x+",y="+y);
//        assertEquals(0,i);
    }

    /**
     * 纯消费
     * 当两个CompletionStage都正常完成计算的时候,执行一个Runnable，这个Runnable并不使用计算的结果。
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void runAfterBoth() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future21 = CompletableFuture.supplyAsync(() -> {
            //long compute
            int i = someBusiness();
            System.out.println("business x result="+i);
            return i;
        },es);

        CompletableFuture<Integer> future22 = CompletableFuture.supplyAsync(() -> {
            //long compute
            int i = someBusiness();
            System.out.println("business y result="+i);
            return i;
        },es);

        future21.runAfterBoth(future22,() -> System.out.println("runAfterBoth proccessing ..."));


        int x = future21.get();
        int y =  future22.get();

        System.out.println("result x=" + x+",y="+y);
//        assertEquals(0,i);
    }

    /**
     * 纯消费
     * 当计算完成的时候会执行一个Runnable,与 thenAccept 不同，Runnable并不使用CompletableFuture计算的结果。
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void thenRun() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future21 = CompletableFuture.supplyAsync(() -> {
            //long compute
            int i = someBusiness();
            System.out.println("business result="+i);
            return i;
        },es);

        future21.thenRun(() -> System.out.println("then run proccessing ..."));

        int i = future21.get();
        System.out.println("result=" + i);
//        assertEquals(0,i);
    }

    /**
     * 组合
     * 接受一个Function作为参数，这个Function的输入是当前的CompletableFuture的计算值，返回结果将是一个新的CompletableFuture，
     * 这个新的CompletableFuture会组合原来的CompletableFuture和函数返回的CompletableFuture。
     * 注意， thenCompose 返回的对象并不一是函数 fn 返回的对象，如果原来的 CompletableFuture 还没有计算出来，它就会生成一个新的组合后的CompletableFuture。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void thenCompose() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future21 = CompletableFuture.supplyAsync(() -> {
            //long compute
            someBusiness();
            return 100;
        },es).thenCompose((i) -> {
            System.out.println("then Compose proccessing ...");

            return CompletableFuture.supplyAsync(() -> i * 10);

        });

        int i = future21.get();
        System.out.println("result=" + i);
        assertEquals(1000,i);
    }

    /**
     * 复合
     * 用来复合另外一个CompletionStage的结果。
     * 两个CompletionStage是并行执行的，它们之间并没有先后依赖顺序， other并不会等待先前的CompletableFuture执行完毕后再执行。
     * 其实从功能上来讲,它们的功能更类似 thenAcceptBoth ，只不过thenAcceptBoth 纯消费，它的函数参数没有返回值，而thenCombine的函数参数fn有返回值。
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void thenCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future21 = CompletableFuture.supplyAsync(() -> {
            //long compute
            return 100;
        },es);

        CompletableFuture<String> future22 = CompletableFuture.supplyAsync(() -> {
            //long compute
            return "hello";
        },es);

        CompletableFuture<String> f =  future21.thenCombine(future22,(x,y) -> {
            System.out.println("then Combine proccessing ...");

            return y + "-" + x;
        });

        String result = f.get();
        System.out.println("result=" + result);
        assertEquals("hello-100",result);
    }

    /**
     * thenAcceptBoth和runAfterBoth是当两个CompletableFuture都计算完成，而acceptEither或acceptEitherAsync方法是acceptEither 方法是当任意一个CompletionStage完成的时候，
     * action这个消费者就会被执行。这个方法返回 CompletableFuture<Void>。
     *
     * applyToEither方法是当任意一个CompletionStage完成的时候，fn会被执行，它的返回值会当作新的CompletableFuture<U>的计算结果。
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void acceptEither() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future21 = CompletableFuture.supplyAsync(() -> {
            //long compute
            Random rand = new Random();
            try
            {
                Thread.sleep(10000+ rand.nextInt(1000));
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        },es);

        CompletableFuture<Integer> future22 = CompletableFuture.supplyAsync(() -> {
            //long compute
            Random rand = new Random();
            try
            {
                Thread.sleep(10000+ rand.nextInt(1000));
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
            return 200;
        },es);

        //构造了两个随机延迟，哪个先返回就打印哪个结果，因此有时返回100，有时返回200
        CompletableFuture<Void> f =  future21.acceptEither(future22,(i) -> {
            System.out.println("then acceptEither proccessing "+i);
        });
        f.get();


        CompletableFuture<Integer> future23 = CompletableFuture.supplyAsync(() -> {
            //long compute
            Random rand = new Random();
            try
            {
                Thread.sleep(10000+ rand.nextInt(1000));
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        },es);

        CompletableFuture<Integer> future24 = CompletableFuture.supplyAsync(() -> {
            //long compute
            Random rand = new Random();
            try
            {
                Thread.sleep(10000+ rand.nextInt(1000));
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
            return 200;
        },es);

        CompletableFuture<String> f1 = future23.applyToEither(future24,i->i.toString());
        String result = f1.get();
        System.out.println("result="+result);
    }


    /**
     * 辅助方法
     * allOf 方法是当所有的CompletableFuture都执行完后执行计算。
     * anyOf 方法是当任意一个CompletableFuture执行完后就会执行计算，计算的结果相同。
     */
    @Test
    public void allOf()
    {
        CompletableFuture<Integer> future21 = CompletableFuture.supplyAsync(() -> {
            //long compute
            Random rand = new Random();
            try
            {
                Thread.sleep(10000+ rand.nextInt(1000));
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        },es);

        CompletableFuture<Integer> future22 = CompletableFuture.supplyAsync(() -> {
            //long compute
            Random rand = new Random();
            try
            {
                Thread.sleep(10000+ rand.nextInt(1000));
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
            return 200;
        },es);

        CompletableFuture<Void> f =  CompletableFuture.allOf(future21,future22);
        try {
            f.get();
            System.out.println("all done!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        CompletableFuture<Integer> future23 = CompletableFuture.supplyAsync(() -> {
            //long compute
            Random rand = new Random();
            try
            {
                Thread.sleep(10000+ rand.nextInt(1000));
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        },es);

        CompletableFuture<Integer> future24 = CompletableFuture.supplyAsync(() -> {
            //long compute
            Random rand = new Random();
            try
            {
                Thread.sleep(10000+ rand.nextInt(1000));
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
            return 200;
        },es);

        CompletableFuture<Object> f1 = CompletableFuture.anyOf(future23,future24);
        Object result = null;
        try {
            result = f1.get();
            System.out.println("result="+result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
