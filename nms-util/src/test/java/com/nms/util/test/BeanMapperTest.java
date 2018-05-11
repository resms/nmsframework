package com.nms.util.test;

import com.nms.util.Convert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class BeanMapperTest{
    public static class Dog {
        private String name;
        private int age;
        private List<String> friends;

        public Dog(String name, int age, List<String> friends) {
            this.name = name;
            this.age = age;
            this.friends = friends;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Dog dog = (Dog) o;

            if (age != dog.age) return false;
            if (!name.equals(dog.name)) return false;
            return friends.equals(dog.friends);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + age;
            result = 31 * result + friends.hashCode();
            return result;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public List<String> getFriends() {
            return friends;
        }

        public void setFriends(List<String> friends) {
            this.friends = friends;
        }
    }
    public static class Cat {
        private String name;
        private int age;
        private List<String> friends;
        private float a;
        private double b;
        private BigDecimal c;
        private Cat parent;

        public Cat(String name, int age, List<String> friends) {
            this.name = name;
            this.age = age;
            this.friends = friends;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cat cat = (Cat) o;

            if (age != cat.age) return false;
            if (!name.equals(cat.name)) return false;
            return friends.equals(cat.friends);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + age;
            result = 31 * result + friends.hashCode();
            return result;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public List<String> getFriends() {
            return friends;
        }

        public void setFriends(List<String> friends) {
            this.friends = friends;
        }

        public float getA() {
            return a;
        }

        public void setA(float a) {
            this.a = a;
        }

        public double getB() {
            return b;
        }

        public void setB(double b) {
            this.b = b;
        }

        public BigDecimal getC() {
            return c;
        }

        public void setC(BigDecimal c) {
            this.c = c;
        }

        public Cat getParent() {
            return parent;
        }

        public void setParent(Cat parent) {
            this.parent = parent;
        }
    }
    @Test
    public void testMap()
    {
        List<String> friends = new ArrayList<String>();
        friends.add("tuoli");
        friends.add("jike");

        Cat cat0 = new Cat("sam",5,friends);
        cat0.setA(66.6f);
        cat0.setB(0.666666);
        cat0.setC(new BigDecimal("43934732479731231"));

        Cat cat1 = new Cat("tom",5,friends);
        cat1.setA(88.8f);
        cat1.setB(0.888888);
        cat1.setC(new BigDecimal("17321983472393249832784328972"));
        cat1.setParent(cat0);

        int loopSize = 100000;
        List<Cat> cats = new ArrayList<Cat>(loopSize);
        long time = System.currentTimeMillis();
        for (int i = 0; i < loopSize;i++) {
            cats.add(Convert.map(cat1, Cat.class));
        }
        System.out.println("runtime " + (System.currentTimeMillis() - time));
        System.out.println(Convert.toJson(cats.get(0)));
        assertTrue(cat1.hashCode() == cats.get(0).hashCode());
    }

    @Test
    public void testMapDiffrent()
    {
        List<String> friends = new ArrayList<String>();
        friends.add("tuoli");
        friends.add("jike");

        List<String> friends1 = new ArrayList<String>();
        friends.add("tuoli1");
        friends.add("jike1");

        Cat cat0 = new Cat("sam",5,friends);
        cat0.setA(66.6f);
        cat0.setB(0.666666);
        cat0.setC(new BigDecimal("43934732479731231"));

        Cat cat1 = new Cat("tom",5,friends);
        cat1.setA(88.8f);
        cat1.setB(0.888888);
        cat1.setC(new BigDecimal("17321983472393249832784328972"));
        cat1.setParent(cat0);

        Cat catX = new Cat("tom",5,friends);
        catX.setA(99.9f);
        catX.setB(0.999999999);
        catX.setC(new BigDecimal("34333333333333333333333"));
        catX.setParent(cat1);

        int loopSize = 100000;
        List<Dog> dogs = new ArrayList<Dog>(loopSize);
        long time = System.currentTimeMillis();
        for (int i = 0; i < loopSize;i++) {
            dogs.add(Convert.map(cat1, Dog.class));
        }
        System.out.println("runtime " + (System.currentTimeMillis() - time));
        System.out.println(Convert.toJson(dogs.get(0)));



        assertTrue(cat1.hashCode() == dogs.get(0).hashCode());
    }
}
