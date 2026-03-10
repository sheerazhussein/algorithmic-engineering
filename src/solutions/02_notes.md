## Table of Contents

1. [Design Contract — Shared Across All Methods](#1-design-contract)
2. [Problem 5 — Largest of Two Floats](#2-problem-5--largest-of-two-floats)
3. [Problem 6 — Multi-Currency Converter](#3-problem-6--multi-currency-converter)
4. [Problem 7 — Fibonacci Series](#4-problem-7--fibonacci-series)
5. [Cross-Cutting Concepts](#5-cross-cutting-concepts)
6. [Quick-Reference Cheatsheet](#6-quick-reference-cheatsheet)

---

## 1. Design Contract

Every method in Challenge_02 follows the same four-rule contract. Understand this once — it applies everywhere.

| Rule | What It Means |
|------|---------------|
| **Return `String` always** | One return type for both results and errors. Caller never branches on type. |
| **Validate first** | Guard clauses at the top. Bad input exits immediately before any logic runs. |
| **Pure function** | `static`, no side effects, no object state. Input in → output out. |
| **Consistent format** | Output is human-readable and uniformly structured across all cases. |

---

## 2. Problem 5 — Largest of Two Floats

### The Core Problem: Why `>`, `<`, `==` Fail on Floats

Floats are stored in memory as **IEEE 754 binary representations**, not simple integers:

```
[ 1 sign bit ][ 8 exponent bits ][ 23 mantissa bits ] = 32 bits total
```

This creates two failure modes that raw operators cannot handle:

**Failure 1 — NaN breaks all comparisons**

```java
Float.NaN > 5.0f      // → false  (expected: error or true)
Float.NaN < 5.0f      // → false  (expected: error or false)
Float.NaN == Float.NaN // → false  (NaN is not equal to itself)
```

NaN is produced by operations like `0.0f / 0.0f` or `Math.sqrt(-1)`. Once NaN enters a comparison, every result is `false`. No exception is thrown. The program silently produces wrong answers.

**Failure 2 — Equality is unreliable**

```java
0.1f + 0.2f == 0.3f  // → false  (rounding error in binary representation)
```

Binary floating-point cannot represent most decimal fractions exactly. Direct equality checks silently fail on values that are mathematically equal.

---

### The Solution

```java
public static String largest(float num1, float num2) {
    // Guard: reject NaN before any comparison
    if (Float.isNaN(num1) || Float.isNaN(num2)) {
        return "At least one input is NaN";
    }

    // Total-order comparison via IEEE 754-compliant method
    int cmp = Float.compare(num1, num2);

    if (cmp > 0)      return "Largest: " + num1;
    else if (cmp < 0) return "Largest: " + num2;
    else              return "Equal: "   + num1;
}
```

---

### Line-by-Line Breakdown

**Guard Clause — `Float.isNaN()`**

```java
if (Float.isNaN(num1) || Float.isNaN(num2)) {
    return "At least one input is NaN";
}
```

`Float.isNaN()` inspects the raw bit pattern: exponent bits all 1s with a non-zero mantissa. It does not use `==` and therefore does not fall into the NaN trap. This runs before `Float.compare()` is ever called, which means NaN can never silently corrupt downstream logic.

**Core Comparison — `Float.compare()`**

```java
int cmp = Float.compare(num1, num2);
```

`Float.compare()` implements IEEE 754 total ordering. Its internal logic:

```
1. Both NaN         → return 0     (treated as equal)
2. Only num1 is NaN → return +1    (NaN sorts last)
3. Only num2 is NaN → return -1
4. Both are +0.0    → return 0
5. +0.0 vs -0.0     → sign-aware comparison
6. Otherwise        → compare raw bit patterns as unsigned integers
```

The return value is always an `int`:

```
cmp > 0   →  num1 is greater
cmp < 0   →  num2 is greater
cmp == 0  →  equal (handles +0.0 vs -0.0 correctly)
```

Why store the result in `cmp` instead of calling `Float.compare()` twice? Because it reads cleaner and eliminates any possibility of calling with different arguments by mistake.

**Three Exhaustive Branches**

```java
if (cmp > 0)      return "Largest: " + num1;
else if (cmp < 0) return "Largest: " + num2;
else              return "Equal: "   + num1;
```

An `int` can only be positive, negative, or zero. These three branches cover every possible state. There is no fourth case.

---

### Edge Case Coverage

| Input | Behavior |
|-------|----------|
| `NaN, 5.0f` | Returns NaN error message immediately |
| `+0.0f, -0.0f` | `Float.compare` returns 0 → "Equal" |
| `Float.POSITIVE_INFINITY, 100f` | Infinity treated as valid number, compared correctly |
| `0.1f + 0.2f, 0.3f` | `Float.compare` handles the tiny difference correctly |
| Same value twice | Returns "Equal: value" |

---

### Production Rule

```
NEVER:  num1 > num2  or  num1 == num2
ALWAYS: Float.isNaN() guard + Float.compare()
```

---

## 3. Problem 6 — Multi-Currency Converter

### The Architecture: USD as a Bridge

Converting between every possible currency pair directly requires storing N×(N-1) rates. With 4 currencies that is 12 rates. With 10 currencies it is 90. This scales poorly.

The bridge pattern uses one currency as a universal intermediate:

```
Source → USD → Target
```

Every currency only needs one rate: its rate against USD. Adding a new currency requires exactly two new lines of code, nothing else.

**Trade-off accepted**: Bridged conversion introduces a tiny rounding difference vs a direct rate. For a learning exercise with hardcoded rates, this is correct. In production, a live rates API would be used instead.

---

### The Solution

```java
public static String multiCurrencyConverter(double amount, String fromCurrency, String toCurrency) {

    // --- Guard Clauses ---
    if (amount < 0)                                    return "Error: Amount cannot be negative";
    if (fromCurrency == null || toCurrency == null)    return "Error: Invalid currencies";

    // --- Normalize ---
    fromCurrency = fromCurrency.toUpperCase();
    toCurrency   = toCurrency.toUpperCase();

    // --- Step 1: Source → USD ---
    double usdAmount;
    switch (fromCurrency) {
        case "USD": usdAmount = amount;          break;  // 1 USD = 1 USD
        case "INR": usdAmount = amount * 0.012;  break;  // 1 INR = 0.012 USD
        case "EUR": usdAmount = amount * 1.08;   break;  // 1 EUR = 1.08 USD
        case "PKR": usdAmount = amount * 0.0036; break;  // 1 PKR = 0.0036 USD
        default:    return "Error: Unsupported source currency: " + fromCurrency;
    }

    // --- Step 2: USD → Target ---
    double convertedAmount;
    switch (toCurrency) {
        case "USD": convertedAmount = usdAmount;         break;  // 1 USD = 1 USD
        case "INR": convertedAmount = usdAmount * 83;    break;  // 1 USD = 83 INR
        case "EUR": convertedAmount = usdAmount * 0.92;  break;  // 1 USD = 0.92 EUR
        case "PKR": convertedAmount = usdAmount * 278;   break;  // 1 USD = 278 PKR
        default:    return "Error: Unsupported target currency: " + toCurrency;
    }

    return String.format("%.2f %s = %.2f %s", amount, fromCurrency, convertedAmount, toCurrency);
}
```

---

### Line-by-Line Breakdown

**`double amount` — not `int` or `float`**

Money involves decimals. `int` truncates. `float` has only 7 significant digits of precision. `double` provides 15–16 significant digits, which is sufficient for all standard financial calculations.

**`null` check before `.toUpperCase()`**

```java
if (fromCurrency == null || toCurrency == null) return "Error: Invalid currencies";
fromCurrency = fromCurrency.toUpperCase();
```

`null` is not an empty string. It is the absence of an object entirely. Calling `.toUpperCase()` on `null` throws `NullPointerException` at runtime. The null check runs first so that `.toUpperCase()` is only ever called on a real object.

**Normalization**

```java
fromCurrency = fromCurrency.toUpperCase();
```

`String` is immutable in Java. `.toUpperCase()` returns a new `String` object. The result is assigned back to the same variable name. The original is discarded. After this line, the switch only needs to handle one form of each currency code.

**`usdAmount` — the staging variable**

```java
double usdAmount;
```

This variable is the bridge. It has no meaning outside the two-step conversion process. Its name communicates its exact role: it holds a USD value in transit. Naming it `x` or `temp` would force the next reader to figure out its purpose from context.

**Switch with `break` and `default`**

Without `break`, Java falls through to the next case. In most switch blocks this is a bug. The `break` exits the switch immediately after the matching case executes.

The `default` case handles all inputs that matched nothing. Including the actual invalid value in the error message (`"Unsupported source currency: " + fromCurrency`) gives specific, actionable feedback instead of a generic error.

**`String.format("%.2f %s = %.2f %s", ...)`**

| Specifier | Meaning |
|-----------|---------|
| `%.2f` | Floating-point number, exactly 2 decimal places |
| `%s` | String, inserted as-is |

Arguments are matched left-to-right: `amount`, `fromCurrency`, `convertedAmount`, `toCurrency`. The output `"100.00 USD = 27800.00 PKR"` is produced without string concatenation or manual rounding.

---

### Tracing a Full Conversion: 100 USD → PKR

```
Step 1: fromCurrency = "USD"
        usdAmount = 100 * 1 = 100.0

Step 2: toCurrency = "PKR"
        convertedAmount = 100.0 * 278 = 27800.0

Output: "100.00 USD = 27800.00 PKR"
```

---

### Scalability: Adding GBP

```java
// Add to switch 1 (source → USD):
case "GBP": usdAmount = amount * 1.27; break;  // 1 GBP = 1.27 USD

// Add to switch 2 (USD → target):
case "GBP": convertedAmount = usdAmount * 0.79; break;  // 1 USD = 0.79 GBP
```

Two lines. Zero other changes. This is the defining characteristic of a scalable design.

---

### Note on the Sample Output Inconsistency

The provided sample shows `50 EUR = 3652.00 INR`. Tracing the actual code:

```
50 EUR × 1.08 = 54.00 USD
54.00 USD × 83 = 4482.00 INR
```

The code produces `4482.00 INR`, not `3652.00`. The sample output was generated with different internal rates than what is written in the code. The code is internally consistent. When a sample output and the code logic conflict, trace the code. The code is the source of truth.

---

## 4. Problem 7 — Fibonacci Series

### What Is the Fibonacci Sequence?

A sequence where each term is the sum of the two terms before it, starting from two defined base values:

```
Base:  0, 1
Then:  0+1=1,  1+1=2,  1+2=3,  2+3=5,  3+5=8 ...
Full:  0  1  1  2  3  5  8  13  21  34 ...
```

The sequence cannot generate itself from nothing. Two seed values are required. Every subsequent term flows deterministically from those two seeds.

---

### The Solution

```java
public static String fibonacciSeries(int n) {

    // --- Guard Clauses ---
    if (n <= 0) return "Error: Number of terms must be positive";
    if (n == 1) return "0";

    // --- Build series ---
    StringBuilder series = new StringBuilder();
    int a = 0, b = 1;

    series.append(a).append(" ");          // Append first term before loop

    for (int i = 1; i < n; i++) {
        series.append(b).append(" ");      // Step 1: print current b
        int next = a + b;                  // Step 2: compute next term
        a = b;                             // Step 3: slide window forward
        b = next;                          // Step 4: slide window forward
    }

    return "Fibonacci (" + n + " terms): " + series.toString().trim();
}
```

---

### Line-by-Line Breakdown

**`int n` — not `double`**

You cannot generate 5.7 terms. The concept of a term count is discrete, not continuous. `int` enforces this at the type level.

**First guard: `n <= 0`**

```java
if (n <= 0) return "Error: Number of terms must be positive";
```

`<=` catches both zero and all negative numbers in one condition. Zero terms has no mathematical meaning. Negative terms has no mathematical meaning.

**Second guard: `n == 1`**

```java
if (n == 1) return "0";
```

The loop appends `b` on every iteration and starts at `i=1`, running `n-1` times. For `n=1`, the loop runs zero times. Only the pre-loop append fires, producing `"0 "` with a trailing space. While `.trim()` would clean this, an explicit guard makes the intent visible and removes any reliance on a downstream side-effect to fix correctness.

**`StringBuilder` instead of `String`**

`String` is immutable. Every `+=` in a loop creates a new object and discards the old one. For `n=100`, that is 100 object allocations and 100 garbage collections, all wasted.

`StringBuilder` is mutable. `.append()` modifies the same object in memory. One allocation, used throughout.

```
String      →  printed page. Cannot be written on.
StringBuilder →  whiteboard. Keep adding until done, then photograph it with .toString().
```

**Two seed variables: `a` and `b`**

```java
int a = 0, b = 1;
```

These represent a sliding window over the sequence. At any moment, `a` is the last printed term and `b` is the next term to print. Only these two values and one temporary variable are needed to generate an infinite sequence. No array, no stored history.

**Pre-loop append**

```java
series.append(a).append(" ");
```

The first term (0, the value of `a`) is appended before the loop. The loop's job is to append `b`. If the first term were appended inside the loop, a conditional would be needed to avoid duplicating it. Separating initialization from iteration avoids that complexity.

`.append(a).append(" ")` is method chaining. `append()` returns the same `StringBuilder` instance, so the second call runs on the same object. Equivalent to two separate lines.

**The Loop — Order Is Not Optional**

```java
for (int i = 1; i < n; i++) {
    series.append(b).append(" ");   // 1. Append current b
    int next = a + b;               // 2. Compute next BEFORE overwriting
    a = b;                          // 3. Slide: old b becomes new a
    b = next;                       // 4. Slide: computed next becomes new b
}
```

Steps 3 and 4 overwrite `a` and `b`. Step 2 must happen before either of them, because the computation `a + b` needs the original values of both variables. The `next` variable exists for exactly this reason: to preserve the sum before the variables it depends on are changed.

If steps were reordered:

```java
a = b;           // a is now overwritten
int next = a + b; // BUG: uses new a, not original a
```

The result would be a wrong sequence. The four-step order is a logical constraint, not a style preference.

**Manual Trace: n = 5**

```
Before loop:  series = "0 ",  a = 0,  b = 1

i=1:  append 1  →  series = "0 1 "    next=0+1=1   a=1  b=1
i=2:  append 1  →  series = "0 1 1 "  next=1+1=2   a=1  b=2
i=3:  append 2  →  series = "0 1 1 2 " next=1+2=3  a=2  b=3
i=4:  append 3  →  series = "0 1 1 2 3 " next=2+3=5 a=3 b=5

Output: "Fibonacci (5 terms): 0 1 1 2 3"
```

**`.trim()` — Clean the Trailing Space**

Every term is appended with a trailing space. After the last term, there is one unwanted space at the end. `.trim()` removes all leading and trailing whitespace in one call. The alternative — a conditional inside the loop to skip the space on the last iteration — would complicate every iteration for the sake of one.

**`.toString()` — Convert StringBuilder to String**

`StringBuilder` is not a `String`. The `+` operator in the return statement works on `String`. `.toString()` returns the accumulated character sequence as a `String` object, at which point `.trim()` and string concatenation work normally.

---

### Complexity Analysis

| Dimension | Value | Reason |
|-----------|-------|--------|
| Time | O(n) | Loop runs exactly n-1 times, constant work per iteration |
| Working space | O(1) | Only 3 integer variables regardless of n |
| Output space | O(n) | StringBuilder grows linearly with n |

The working space is O(1) because the algorithm never stores the sequence in an array. It generates each term, appends it, and reuses the same three variables for the next term.

---

### Why Not Recursion?

The naive recursive definition mirrors the mathematical definition:

```java
int fib(int n) {
    if (n <= 1) return n;
    return fib(n - 1) + fib(n - 2);
}
```

This is clean to read but catastrophically slow. Computing `fib(8)` requires computing `fib(7)` and `fib(6)`. Computing `fib(7)` requires computing `fib(6)` again. Each level of the tree duplicates work exponentially.

```
Time complexity of naive recursive Fibonacci: O(2^n)
Time complexity of this iterative solution:   O(n)

For n=40:  2^40  ≈ 1,099,511,627,776 operations
           n=40  =                  40 operations
```

For generating a series, iteration is always the correct choice over naive recursion.

---

## 5. Cross-Cutting Concepts

### Guard Clause Pattern

All three methods use guard clauses: validation at the top, early return on failure.

```java
// Without guard clauses (deeply nested, hard to read)
public static String method(int x) {
    if (x > 0) {
        if (x < 100) {
            // actual logic buried here
        } else {
            return "Error: too large";
        }
    } else {
        return "Error: negative";
    }
}

// With guard clauses (flat, easy to read)
public static String method(int x) {
    if (x <= 0)   return "Error: negative";
    if (x >= 100) return "Error: too large";
    // actual logic runs here, unindented
}
```

Guard clauses eliminate nesting by handling failure cases first and returning immediately. The happy path runs last, unobstructed.

---

### Unified Return Type

Every method returns `String` for both success and failure. This eliminates the need for exception handling or null checks in the calling code.

```java
// Caller is always simple:
System.out.println("Answer: " + methodName(args));

// No branching required:
// String result = method(args);
// if (result == null) { ... }
// try { ... } catch (Exception e) { ... }
```

---

### `static` Method Design

All methods are `static`. They take inputs, compute outputs, and touch no external state. This is the pure function pattern. Pure functions are predictable, testable, and reusable because their output depends only on their inputs.

---

### `String.format` vs String Concatenation

| Approach | Code | Readability |
|----------|------|-------------|
| Concatenation | `"" + a + " " + b + " = " + c + " " + d` | Fragile, error-prone |
| `String.format` | `String.format("%.2f %s = %.2f %s", a, b, c, d)` | Template is clear |

`String.format` separates the structure (the template) from the data (the arguments). The template can be read and understood independently of the values being inserted.

---

## 6. Quick-Reference Cheatsheet

### Float Comparison

```java
// WRONG
if (a > b) ...
if (a == b) ...

// CORRECT
if (Float.isNaN(a) || Float.isNaN(b)) return "NaN error";
int cmp = Float.compare(a, b);
// cmp > 0 → a is larger | cmp < 0 → b is larger | cmp == 0 → equal
```

### Currency Conversion

```java
// Pattern: source → USD (step 1) → target (step 2)
// Rates are hardcoded. Comments explain each magic number.
// default case returns specific error with unsupported currency name.
```

### Fibonacci Loop Order

```
1. Append b
2. next = a + b      ← MUST be before reassignment
3. a = b
4. b = next
```

### `StringBuilder` Rule

```
Use String     → short, non-looped assembly
Use StringBuilder → any loop that builds a string iteratively
Call .toString().trim() to finalize
```

### Format Specifiers

| Specifier | Type | Example Output |
|-----------|------|----------------|
| `%.2f` | `double` / `float` | `100.00` |
| `%d` | `int` | `42` |
| `%s` | `String` | `USD` |
| `%n` | newline | (platform-safe newline) |

---

