# Solution_02 — Complete Deep Notes

## Table of Contents

1. [Design Contract — Shared Across All Methods](#1-design-contract)
2. [Problem 5 — Largest of Two Floats](#2-problem-5--largest-of-two-floats)
3. [Problem 6 — Multi-Currency Converter](#3-problem-6--multi-currency-converter)
4. [Problem 7 — Fibonacci Series](#4-problem-7--fibonacci-series)
5. [Problem 8 — Palindrome Checker](#5-problem-8--palindrome-checker)
6. [Problem 9 — Armstrong Numbers](#6-problem-9--armstrong-numbers)
7. [Cross-Cutting Concepts](#7-cross-cutting-concepts)
8. [Quick-Reference Cheatsheet](#8-quick-reference-cheatsheet)

---

## 1. Design Contract

Every method in Solution_02 follows the same four-rule contract. Understand this once — it applies everywhere.

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
Float.NaN > 5.0f       // → false  (expected: error or true)
Float.NaN < 5.0f       // → false  (expected: error or false)
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
    if (amount < 0)                                 return "Error: Amount cannot be negative";
    if (fromCurrency == null || toCurrency == null) return "Error: Invalid currencies";

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

This variable is the bridge. Its name communicates its exact role: it holds a USD value in transit between the two conversion steps. Naming it `x` or `temp` would force the next reader to figure out its purpose from context.

**Switch with `break` and `default`**

Without `break`, Java falls through to the next case. The `break` exits the switch immediately after the matching case executes.

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
        usdAmount = 100 × 1 = 100.0

Step 2: toCurrency = "PKR"
        convertedAmount = 100.0 × 278 = 27800.0

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
        series.append(b).append(" ");      // Step 1: append current b
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

The loop appends `b` on every iteration and starts at `i=1`, running `n-1` times. For `n=1`, the loop runs zero times and only the pre-loop append fires, producing `"0 "` with a trailing space. An explicit guard makes the intent visible and removes reliance on a downstream side-effect to fix correctness.

**`StringBuilder` instead of `String`**

`String` is immutable. Every `+=` in a loop creates a new object and discards the old one. For `n=100`, that is 100 object allocations — all wasted.

`StringBuilder` is mutable. `.append()` modifies the same object in memory. One allocation, used throughout.

```
String        →  printed page. Cannot be written on.
StringBuilder →  whiteboard. Keep adding until done, then photograph it with .toString().
```

**Two seed variables: `a` and `b`**

```java
int a = 0, b = 1;
```

These represent a **sliding window** over the sequence. At any moment, `a` is the last printed term and `b` is the next term to print. Only these two values and one temporary variable are needed to generate an infinite sequence. No array, no stored history.

**Pre-loop append**

```java
series.append(a).append(" ");
```

The first term (0) is appended before the loop. The loop's job is to append `b`. If the first term were appended inside the loop, a conditional would be needed to avoid duplicating it. Separating initialization from iteration avoids that complexity.

**The Loop — Order Is Not Optional**

```java
for (int i = 1; i < n; i++) {
    series.append(b).append(" ");   // 1. Append current b
    int next = a + b;               // 2. Compute next BEFORE overwriting
    a = b;                          // 3. Slide: old b becomes new a
    b = next;                       // 4. Slide: computed next becomes new b
}
```

Steps 3 and 4 overwrite `a` and `b`. Step 2 must happen before either of them, because `a + b` needs the original values of both variables. The `next` variable exists to preserve the sum before the variables it depends on are changed.

If reordered incorrectly:

```java
a = b;            // a is now overwritten
int next = a + b; // BUG: uses new a, not original a
```

The result would be a wrong sequence. The four-step order is a logical constraint, not a style preference.

**Manual Trace: n = 5**

```
Before loop:  series = "0 ",  a = 0,  b = 1

i=1:  append 1  →  "0 1 "      next=0+1=1   a=1  b=1
i=2:  append 1  →  "0 1 1 "    next=1+1=2   a=1  b=2
i=3:  append 2  →  "0 1 1 2 "  next=1+2=3   a=2  b=3
i=4:  append 3  →  "0 1 1 2 3 " next=2+3=5  a=3  b=5

Output: "Fibonacci (5 terms): 0 1 1 2 3"
```

---

### Complexity Analysis

| Dimension | Value | Reason |
|-----------|-------|--------|
| Time | O(n) | Loop runs exactly n-1 times, constant work per iteration |
| Working space | O(1) | Only 3 integer variables regardless of n |
| Output space | O(n) | StringBuilder grows linearly with n |

---

### Why Not Recursion?

```java
int fib(int n) {
    if (n <= 1) return n;
    return fib(n - 1) + fib(n - 2);  // recomputes same values exponentially
}
```

```
Time: naive recursive = O(2ⁿ)
Time: this solution   = O(n)

For n=40:  2⁴⁰ ≈ 1,099,511,627,776 operations
           n=40 =                 40 operations
```

For generating a series, iteration is always the correct choice over naive recursion.

---

## 5. Problem 8 — Palindrome Checker

### What Is a Palindrome?

A palindrome is a string that reads the same forward and backward.

```
"radar"  →  forward: r-a-d-a-r  |  backward: r-a-d-a-r  →  same ✅
"hello"  →  forward: h-e-l-l-o  |  backward: o-l-l-e-h  →  different ❌
"A"      →  single character    →  trivially same ✅
```

The simplest mechanical test: reverse the string, then compare. If the reversed version is identical to the original, it is a palindrome.

---

### The Solution

```java
public static String isPalindrome(String str) {

    // --- Guard Clause ---
    if (str == null || str.isEmpty()) {
        return "Error: Empty string";
    }

    // --- Step 1: Build reversed string ---
    String reversed = "";
    for (int i = str.length() - 1; i >= 0; i--) {
        reversed = reversed + str.charAt(i);
    }

    // --- Step 2: Compare ---
    if (str.equals(reversed)) {
        return str + " is Palindrome";
    } else {
        return str + " is Not Palindrome";
    }
}
```

---

### Line-by-Line Breakdown

**Guard Clause — null before isEmpty**

```java
if (str == null || str.isEmpty()) {
    return "Error: Empty string";
}
```

Two separate failure conditions caught in one guard. `null` means no object exists at all. Calling `.isEmpty()` on `null` throws `NullPointerException`. The null check must come first because Java evaluates `||` left to right and stops as soon as one side is `true` — if `str` is null, the right side is never evaluated.

```java
// SAFE — null check first
if (str == null || str.isEmpty())

// CRASH if str is null
if (str.isEmpty() || str == null)
```

**`str.length() - 1` — zero-based indexing**

Strings are zero-indexed. A string of length 5 has indices 0–4. The last character is at index `length - 1`. Starting at `str.length()` would be one position past the end — a `StringIndexOutOfBoundsException`.

**`i >= 0` — the loop boundary**

Index 0 is the first character. When `i` reaches -1, the condition is false and the loop stops. Every character is visited exactly once.

**`str.charAt(i)` — character access**

Returns the single character at position `i`. This is the foundation of all character-by-character string processing in Java.

**`.equals()` vs `==` — the critical distinction**

```java
if (str.equals(reversed))
```

`==` on objects compares references — are these two variables pointing to the same object in memory? Two different `String` objects with identical content will have different addresses. `==` returns `false` even when content matches.

`.equals()` compares content — do both strings contain the same character sequence? This is always what you want for string comparison.

```java
String a = "radar";
String b = "radar";
a == b       // unreliable — may be true or false depending on JVM internals
a.equals(b)  // always true — content comparison
```

**Return format includes original string**

```java
return str + " is Palindrome";
return str + " is Not Palindrome";
```

Including the original string in the message gives context. `"radar is Palindrome"` is more informative than just `"is Palindrome"`.

---

### Case Sensitivity

This implementation is case-sensitive. `'R'` and `'r'` are different characters (Unicode code points 82 and 114).

```
"RaDaR"  →  reversed = "RaDaR"  →  equal ✅  (symmetric case)
"Radar"  →  reversed = "radaR"  →  not equal ❌
```

To make it case-insensitive, normalize first:

```java
str = str.toLowerCase();
```

That is a deliberate design change, not a bug fix.

---

### Known Limitation: String Concatenation in a Loop

```java
reversed = reversed + str.charAt(i);
```

`String` is immutable. This creates a brand new `String` object on every iteration, copying all previous content in. For a string of length `n`, this creates `n` intermediate objects.

The production fix:

```java
String reversed = new StringBuilder(str).reverse().toString();
```

For this exercise, the explicit loop is used because it makes the algorithm visible and traceable. `StringBuilder.reverse()` hides the mechanism. Learning comes from seeing the steps.

---

### Tracing the Loop: "radar"

```
Initial:  reversed = ""

i=4:  charAt(4)='r'  →  reversed = "r"
i=3:  charAt(3)='a'  →  reversed = "ra"
i=2:  charAt(2)='d'  →  reversed = "rad"
i=1:  charAt(1)='a'  →  reversed = "rada"
i=0:  charAt(0)='r'  →  reversed = "radar"

"radar".equals("radar") → true
Output: "radar is Palindrome"
```

---

### Complexity Analysis

| Dimension | Value | Reason |
|-----------|-------|--------|
| Time | O(n) | One loop pass of n iterations + one `.equals()` scan |
| Space | O(n) | `reversed` string grows to the same length as input |

---

### Edge Case Coverage

| Input | Output | Reason |
|-------|--------|--------|
| `"radar"` | `"radar is Palindrome"` | Reversed equals original |
| `"hello"` | `"hello is Not Palindrome"` | Reversed differs |
| `"A"` | `"A is Palindrome"` | Single character is trivially a palindrome |
| `"aa"` | `"aa is Palindrome"` | Two identical characters |
| `"ab"` | `"ab is Not Palindrome"` | Two different characters |
| `""` | `"Error: Empty string"` | Guard clause catches empty |
| `null` | `"Error: Empty string"` | Guard clause catches null first |
| `"RaDaR"` | `"RaDaR is Palindrome"` | Symmetric case matches |
| `"Radar"` | `"Radar is Not Palindrome"` | `'R'` ≠ `'r'` |

---

## 6. Problem 9 — Armstrong Numbers

### What Is an Armstrong Number?

An Armstrong number (also called a Narcissistic number) equals the sum of its own digits, each raised to the power of the total digit count.

```
Formula:  n = d₁ᵏ + d₂ᵏ + ... + dₖᵏ

Where:  n = the original number
        d = each digit
        k = total number of digits (the power)
```

**Example: 153**

```
Digits = 3  →  power = 3

1³ + 5³ + 3³
= 1 + 125 + 27
= 153  ✅
```

---

### The Solution

```java
public static String armstrongNumbers(int start, int end) {

    // --- Guard Clauses ---
    if (start < 0 || end < 0) return "Error: Range values must be non-negative";
    if (start > end)           return "Error: Start must be less than or equal to end";

    StringBuilder result = new StringBuilder();

    for (int n = start; n <= end; n++) {

        // Step 1: Count digits → this is the power
        int digits = 0;
        int temp   = n;
        while (temp > 0) {
            digits++;
            temp /= 10;
        }
        if (n == 0) digits = 1;  // edge case: 0 has 1 digit

        // Step 2: Extract each digit, raise to power, accumulate
        int sum = 0;
        temp = n;
        while (temp > 0) {
            int digit = temp % 10;
            sum      += (int) Math.pow(digit, digits);
            temp     /= 10;
        }

        // Step 3: Compare sum with original
        if (sum == n) {
            result.append(n).append(" ");
        }
    }

    String found = result.toString().trim();
    if (found.isEmpty()) return "No Armstrong numbers found between " + start + " and " + end;

    return "Armstrong Numbers (" + start + " to " + end + "): " + found;
}
```

---

### Line-by-Line Breakdown

**Guard Clauses**

```java
if (start < 0 || end < 0) return "Error: Range values must be non-negative";
if (start > end)           return "Error: Start must be less than or equal to end";
```

Two separate invalid states. Negative ranges have no meaning for Armstrong numbers. An inverted range (start > end) means the loop would run zero iterations — catching it explicitly produces a clear error instead of silent empty output.

**Why `temp` instead of `n` directly**

```java
int temp = n;
while (temp > 0) { ... temp /= 10; }
```

The while loop destroys its variable through repeated division by 10. `n` must be preserved for the final comparison (`sum == n`). `temp` is a sacrificial copy — it is the variable we are allowed to destroy.

**Step 1 — Counting digits (= the power)**

```java
int digits = 0;
int temp = n;
while (temp > 0) {
    digits++;
    temp /= 10;
}
if (n == 0) digits = 1;
```

Dividing by 10 in integer arithmetic removes the last digit. The count of how many times this can be done before reaching zero equals the number of digits. This count must be captured before Step 2, because it is the fixed power used for every digit in Step 2.

The `n == 0` edge case: the while loop never runs for zero (condition `0 > 0` is immediately false). But 0 has one digit, and `0¹ = 0`, so 0 is technically an Armstrong number. The explicit fix sets `digits = 1` for this case.

**The Two Core Operations**

```java
int digit = temp % 10;   // extract last digit
temp /= 10;              // chop last digit
```

`% 10` returns the remainder after dividing by 10 — always the units digit. `/ 10` (integer division) shifts all digits one position right, discarding the units digit. Together, these two operations are the universal mechanism for digit-by-digit number decomposition.

```
temp = 153
153 % 10 = 3   →  digit extracted
153 / 10 = 15  →  15 remains

temp = 15
15 % 10 = 5    →  digit extracted
15 / 10 = 1    →  1 remains

temp = 1
1 % 10 = 1     →  digit extracted
1 / 10 = 0     →  loop stops
```

**`Math.pow(digit, digits)` cast to `int`**

```java
sum += (int) Math.pow(digit, digits);
```

`Math.pow()` returns a `double`. The result is cast to `int` because we are working with whole-number sums and comparing to an `int` at the end. The cast is safe here because Armstrong numbers only involve integer digits raised to small integer powers — no fractional component is lost.

**`StringBuilder` for accumulation**

The outer loop may add many Armstrong numbers to the result. Using `StringBuilder` avoids the object-creation overhead of repeated `String` concatenation in a loop (same reason as Fibonacci).

**Empty result check**

```java
if (found.isEmpty()) return "No Armstrong numbers found between " + start + " and " + end;
```

A range with no Armstrong numbers (such as 100–152) should produce a meaningful message, not blank output. This check gives the caller actionable information.

---

### Dry Run: Is 1634 Armstrong?

```
n = 1634,  power = 4

Iteration 1:  digit = 1634 % 10 = 4   sum = 0 + 4⁴ = 256       temp = 163
Iteration 2:  digit = 163 % 10  = 3   sum = 256 + 3⁴ = 337     temp = 16
Iteration 3:  digit = 16 % 10   = 6   sum = 337 + 6⁴ = 1633    temp = 1
Iteration 4:  digit = 1 % 10    = 1   sum = 1633 + 1⁴ = 1634   temp = 0

sum (1634) == n (1634)  ✅  Armstrong Number
```

---

### All Armstrong Numbers: 1 to 9999

| Number | Digits | Calculation |
|--------|--------|-------------|
| 1–9 | 1 | n¹ = n (all trivially Armstrong) |
| 153 | 3 | 1³ + 5³ + 3³ = 1 + 125 + 27 = 153 |
| 370 | 3 | 3³ + 7³ + 0³ = 27 + 343 + 0 = 370 |
| 371 | 3 | 3³ + 7³ + 1³ = 27 + 343 + 1 = 371 |
| 407 | 3 | 4³ + 0³ + 7³ = 64 + 0 + 343 = 407 |
| 1634 | 4 | 1⁴ + 6⁴ + 3⁴ + 4⁴ = 1 + 1296 + 81 + 256 = 1634 |
| 8208 | 4 | 8⁴ + 2⁴ + 0⁴ + 8⁴ = 4096 + 16 + 0 + 4096 = 8208 |
| 9474 | 4 | 9⁴ + 4⁴ + 7⁴ + 4⁴ = 6561 + 256 + 2401 + 256 = 9474 |

---

### Complexity Analysis

| Dimension | Value | Reason |
|-----------|-------|--------|
| Time | O(n × d) | For each number n in range, d = digit count (at most 10) |
| Space | O(1) working | Fixed variables per iteration, no arrays |
| Output space | O(k) | StringBuilder grows by count of Armstrong numbers found |

In practice, `d` is bounded by a small constant (≤10 for 32-bit int), so the algorithm is effectively O(n) over the range.

---

### Edge Case Coverage

| Input | Output |
|-------|--------|
| `0, 9` | All single digits (0–9 are all Armstrong) |
| `100, 152` | "No Armstrong numbers found..." |
| `153, 153` | `"Armstrong Numbers (153 to 153): 153"` |
| `-1, 100` | Error: Range values must be non-negative |
| `500, 100` | Error: Start must be less than or equal to end |

---

## 7. Cross-Cutting Concepts

### Guard Clause Pattern

All methods use guard clauses: validation at the top, early return on failure.

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

Every method returns `String` for both success and failure. This eliminates exception handling or null checks in the calling code.

```java
// Caller is always simple — no branching required:
System.out.println("Answer: " + methodName(args));
```

---

### `static` Method Design

All methods are `static`. They take inputs, compute outputs, and touch no external state. Pure functions are predictable, testable, and reusable because their output depends only on their inputs.

---

### `String.format` vs String Concatenation

| Approach | Code | Readability |
|----------|------|-------------|
| Concatenation | `"" + a + " " + b + " = " + c + " " + d` | Fragile, hard to scan |
| `String.format` | `String.format("%.2f %s = %.2f %s", a, b, c, d)` | Template is self-documenting |

`String.format` separates the structure (template) from the data (arguments). The template can be read and understood independently of the values being inserted.

---

### The `temp` Variable Pattern

Across Armstrong, Fibonacci, and Palindrome: whenever an algorithm must consume or destroy a value through a loop, a copy (`temp`) is made so the original is preserved for later use (final comparison, output, etc.).

```java
int temp = n;    // copy to destroy
while (temp > 0) {
    // consume temp
}
// n is still intact for comparison
if (sum == n) { ... }
```

---

## 8. Quick-Reference Cheatsheet

### Float Comparison

```java
// WRONG
if (a > b) ...
if (a == b) ...

// CORRECT
if (Float.isNaN(a) || Float.isNaN(b)) return "NaN error";
int cmp = Float.compare(a, b);
// cmp > 0 → a larger | cmp < 0 → b larger | cmp == 0 → equal
```

### Currency Conversion Pattern

```
Source currency → USD (step 1) → Target currency (step 2)
Each currency needs exactly one rate vs USD.
default case in switch → specific error with unsupported currency name.
```

### Fibonacci Loop Order

```
1. Append b              ← print current
2. next = a + b          ← compute BEFORE overwriting
3. a = b                 ← slide window
4. b = next              ← slide window
```

### Palindrome Key Rules

```
1. null check BEFORE isEmpty()
2. Last index = str.length() - 1  (not str.length())
3. Always use .equals(), never == for String content
4. Case-sensitive by default
```

### Armstrong Core Operations

```
digit = temp % 10      ← extract last digit
temp  = temp / 10      ← remove last digit
sum  += digit ^ power  ← accumulate

Count digits FIRST (before extraction) → this is the power.
Preserve original n with a temp copy.
Handle n=0 separately (while loop never runs for 0).
```

### `StringBuilder` Rule

```
Use String        → short, non-looped assembly
Use StringBuilder → any loop that builds a string iteratively
Finalize with     → .toString().trim()
```

### Format Specifiers

| Specifier | Type | Example Output |
|-----------|------|----------------|
| `%.2f` | `double` / `float` | `100.00` |
| `%d` | `int` | `42` |
| `%s` | `String` | `USD` |
| `%n` | newline | (platform-safe newline) |

---

*Solution_02 Deep Notes — Problems 5 through 9*