package com.nms.util.test;

/**
 * Created by sam on 17-2-22.
 */
public interface Car {

    static class CarBody{
        private int length;
        private int width;
        private int height;
        private int wheelbase;

        public CarBody(int length, int width, int height, int wheelbase) {
            this.length = length;
            this.width = width;
            this.height = height;
            this.wheelbase = wheelbase;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWheelbase() {
            return wheelbase;
        }

        public void setWheelbase(int wheelbase) {
            this.wheelbase = wheelbase;
        }
    }

    static enum LevelType {
        A,A0,B,SUV
    }

    CarBody body(LevelType et);

    default String di()
    {
        return "di";
    }
}
