package me.rotthin.projectm.engine.math;

import org.joml.Vector2f;

public final class JMath {
    private JMath() {}

    public static void rotate(Vector2f a_vec, float a_deg, Vector2f a_org) {
        float x = a_vec.x - a_org.x;
        float y = a_vec.y - a_org.y;

        float cos = (float)Math.cos(Math.toRadians(a_deg));
        float sin = (float)Math.sin(Math.toRadians(a_deg));

        float xPrime = (x * cos) - (y * sin);
        float yPrime = (x * sin) + (y * cos);

        xPrime += a_org.x;
        yPrime += a_org.y;

        a_vec.x = xPrime;
        a_vec.y = yPrime;
    }

    public static boolean compare(float a_x, float a_y, float a_epsilon) {
        return Math.abs(a_x - a_y) <= a_epsilon * Math.max(1.0f, Math.max(Math.abs(a_x), Math.abs(a_y)));
    }

    public static boolean compare(Vector2f a_vec1, Vector2f a_vec2, float a_epsilon) {
        return compare(a_vec1.x, a_vec2.x, a_epsilon) && compare(a_vec1.y, a_vec2.y, a_epsilon);
    }

    public static boolean compare(float a_x, float a_y) {
        return Math.abs(a_x - a_y) <= Float.MIN_VALUE * Math.max(1.0f, Math.max(Math.abs(a_x), Math.abs(a_y)));
    }

    public static boolean compare(Vector2f a_vec1, Vector2f a_vec2) {
        return compare(a_vec1.x, a_vec2.x) && compare(a_vec1.y, a_vec2.y);
    }

    public static String vector2fToString(Vector2f a_str){
        String _x = Float.toString(a_str.x);
        String _y = Float.toString(a_str.y);

        return "x:" + _x + ", y:" + _y;
    }
}