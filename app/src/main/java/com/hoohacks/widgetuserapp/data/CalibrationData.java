package com.hoohacks.widgetuserapp.data;

public class CalibrationData {

    Acc Acc;
    RE RE;
    Flex Flex;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Acc getAcc() {
        return Acc;
    }

    public void setAcc(Acc acc) {
        Acc = acc;
    }

    public RE getRE() {
        return RE;
    }

    public void setRE(RE RE) {
        this.RE = RE;
    }

    public Flex getFlex() {
        return Flex;
    }

    public void setFlex(Flex flex) {
        Flex = flex;
    }

    public static class RE{
        float high, low;

        public float getHigh() {
            return high;
        }

        public void setHigh(float high) {
            this.high = high;
        }

        public float getLow() {
            return low;
        }

        public void setLow(float low) {
            this.low = low;
        }
    }

    public static class Flex{
        float high, low;

        public float getHigh() {
            return high;
        }

        public void setHigh(float high) {
            this.high = high;
        }

        public float getLow() {
            return low;
        }

        public void setLow(float low) {
            this.low = low;
        }
    }

    public static class Acc{

        x x;
        y y;
        z z;

        public x getX() {
            return x;
        }

        public void setX(x x) {
            this.x = x;
        }

        public y getY() {
            return y;
        }

        public void setY(y y) {
            this.y = y;
        }

        public z getZ() {
            return z;
        }

        public void setZ(z z) {
            this.z = z;
        }

        public static class x{
            float high, low;

            public float getHigh() {
                return high;
            }

            public void setHigh(float high) {
                this.high = high;
            }

            public float getLow() {
                return low;
            }

            public void setLow(float low) {
                this.low = low;
            }
        }

        public static class y{
            float high, low;

            public float getHigh() {
                return high;
            }

            public void setHigh(float high) {
                this.high = high;
            }

            public float getLow() {
                return low;
            }

            public void setLow(float low) {
                this.low = low;
            }
        }

        public static class z{
            float high, low;

            public float getHigh() {
                return high;
            }

            public void setHigh(float high) {
                this.high = high;
            }

            public float getLow() {
                return low;
            }

            public void setLow(float low) {
                this.low = low;
            }
        }
    }
}
