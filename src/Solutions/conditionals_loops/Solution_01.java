package Solutions.conditionals_loops;

public class Solution_01 {

    public static void main(String[] args) {
        System.out.printf("Area of Circle (r=5):          %.4f%n", areaOfCircle(5));

    }

    /* A01. AREA OF CIRCLE
     * ──────────────────────────────────────────────────────────
     * FIRST PRINCIPLE:
     *   A circle is every point exactly distance r from the center.
     *   If you tile a circle with infinitely thin rings of width dr,
     *   each ring has circumference 2πr and area 2πr·dr.
     *   Integrating: ∫₀ʳ 2πr dr = πr²  ← this IS the formula.
     *
     * WHY π (pi)?
     *   π = circumference / diameter for ANY circle.
     *   It's irrational (3.14159265...) — never terminates.
     *   Math.PI gives the closest double representation.
     *
     * WHY double?
     *   π itself is infinite. We need maximum decimal precision.
     *   double = 64-bit IEEE 754 → ~15 significant digits.
     *   float = 32-bit → only ~7 digits. Not enough for precision.
     *
     * FORMULA: A = π × r²
     * ──────────────────────────────────────────────────────────
     */
    public static double areaOfCircle(double radius) {
        if (radius < 0) throw new IllegalArgumentException("Radius cannot be negative");
        return Math.PI * radius * radius;
        // Math.PI = 3.141592653589793 (best 64-bit approximation of π)
        // radius * radius = r² (squaring: multiply the number by itself)
    }

}

