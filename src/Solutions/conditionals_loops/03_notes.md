ALGORITHMIC ENGINEERING — COMPLETE FIRST PRINCIPLES DEEP DIVE
Part 1: Foundation + Basic Programs (Area & Perimeter)

═══════════════════════════════════════════════════════════
ABSOLUTE FOUNDATION — BEFORE ANY CODE
═══════════════════════════════════════════════════════════
What Is a Computer, Really?
Your computer is, at its absolute core, a machine that switches electricity
on and off extremely fast. That's it. Everything else — your videos, your
apps, this code — is an elaborate consequence of that single fact.
The Transistor: The Atom of Computing
A transistor is a tiny semiconductor switch made of silicon. It has three
terminals: source, gate, drain. When voltage is applied to the gate, current
flows from source to drain (ON). When no voltage: no current (OFF).
Gate (voltage in)
│
Source ────┤──── Drain
(current in)    (current out)

Gate = HIGH voltage → switch ON  → current flows → represents 1
Gate = LOW  voltage → switch OFF → no current   → represents 0
A modern CPU has ~50 BILLION transistors on a chip the size of your
thumbnail. They switch 3-5 billion times per second (3-5 GHz clock speed).
Why Binary (0 and 1)?
Because transistors only have 2 reliable states (ON/OFF). We could build
computers in decimal (10 states), but distinguishing 10 voltage levels
(0V, 0.5V, 1V, 1.5V...) is physically unreliable. Two states is maximally
noise-resistant. This is why ALL computing is binary.
Binary Number System: Counting in Base 2
Humans count in base 10 because we have 10 fingers. Each digit position
represents a power of 10:
Decimal 425:
4 × 10²  +  2 × 10¹  +  5 × 10⁰
= 4 × 100  +  2 × 10   +  5 × 1
= 400      +  20        +  5
= 425
Computers count in base 2. Each digit position represents a power of 2:
Binary 10011001:
1×2⁷ + 0×2⁶ + 0×2⁵ + 1×2⁴ + 1×2³ + 0×2² + 0×2¹ + 1×2⁰
= 128  +  0   +  0   +  16  +  8   +  0   +  0   +  1
= 153

So the number 153 in RAM looks like: 10011001
RAM: The Giant Shelf of Boxes
RAM (Random Access Memory) is a grid of capacitors. A charged capacitor
stores a 1, discharged stores a 0. Each capacitor = 1 bit. 8 bits = 1 byte.
Every byte has an address (like a house number on a street).
Address:  1000    1001    1002    1003    1004    ...
Content:  10011001 00000000 00000101 11110000 ...
[  153 ] [   0  ] [   5  ] [ 240  ] ...
When you write int num = 153 in Java:

Java requests 4 bytes (32 bits) from the OS
OS finds free addresses, say 1000-1003
Java converts 153 to binary: 00000000 00000000 00000000 10011001
Stores those 32 bits across bytes 1000-1003
Remembers that "num" refers to address 1000

The CPU: The Brain That Executes Instructions
The CPU has:

ALU (Arithmetic Logic Unit): Does math (add, subtract, multiply, compare)
Registers: Tiny ultra-fast storage (only ~16 of them, but blazing fast)
Control Unit: Fetches instructions from memory and executes them
Cache: Small fast memory between CPU and RAM (L1/L2/L3)

Every Java statement compiles down to machine instructions like:
MOV  R1, [address of num]    ; load num into register R1
MOV  R2, 2                   ; load constant 2 into R2
DIV  R3, R1, R2              ; R3 = R1 / R2 (divide)
CMP  R3, 0                   ; compare result with 0
JZ   label_even              ; if zero, jump to "even" label
; ... else it's odd
Data Types: How Many Bytes Per Variable?
Type      | Bits | Bytes | Range
──────────┼──────┼───────┼──────────────────────────────────
byte      |  8   |   1   | -128 to 127
short     | 16   |   2   | -32,768 to 32,767
int       | 32   |   4   | -2,147,483,648 to 2,147,483,647
long      | 64   |   8   | -9.2 × 10¹⁸ to 9.2 × 10¹⁸
float     | 32   |   4   | ~7 decimal digits of precision
double    | 64   |   8   | ~15 decimal digits of precision
char      | 16   |   2   | 0 to 65,535 (Unicode character)
boolean   |  1   |   1   | true or false
WHY these specific sizes? They're powers of 2 because binary doubling
is natural in hardware. 8-bit bytes became the industry standard in the 1960s.
IEEE 754: How Decimals Are Stored
3.14 cannot be represented exactly in binary (it repeats like 1/3 in decimal).
IEEE 754 standard uses scientific notation in binary:
float (32 bits):
┌─┬────────┬───────────────────────┐
│S│EEEEEEEE│MMMMMMMMMMMMMMMMMMMMMMM│
└─┴────────┴───────────────────────┘
1   8 bits        23 bits
Sign Exponent      Mantissa (fraction)

Value = (-1)^S × 1.MMMMM × 2^(E-127)

3.14 in float ≈ 3.1400001049... (NOT exactly 3.14!)
This is why:
javaSystem.out.println(0.1 + 0.2);   // prints 0.30000000000000004 !!
0.1 in binary = 0.000110011001100... (repeating forever, gets truncated).
This is floating point imprecision — a fundamental physical limitation.
The Call Stack: How Methods Work in Memory
Memory is divided into regions:
HIGH ADDRESSES
┌──────────────────┐
│      STACK       │ ← method call frames (grows downward)
│    (grows ↓)     │
├──────────────────┤
│                  │
│    (free space)  │
│                  │
├──────────────────┤
│      HEAP        │ ← objects, arrays (grows upward)
│    (grows ↑)     │
├──────────────────┤
│  STATIC / DATA   │ ← static variables, string pool
├──────────────────┤
│      CODE        │ ← your compiled bytecode
└──────────────────┘
LOW ADDRESSES
When you call areaOfCircle(5.0):

CPU pushes a new "stack frame" onto the stack
Frame contains: radius=5.0, return address, local variables
Method executes using those local copies
Returns result, frame is POPPED (destroyed), memory freed
Execution resumes where it left off

Parameters are COPIED into the frame. This is "pass by value" — changing
radius inside the method does NOT change the caller's variable.

═══════════════════════════════════════════════════════════
BASIC PROGRAM A01: AREA OF CIRCLE
═══════════════════════════════════════════════════════════
javapublic static double areaOfCircle(double radius) {
if (radius < 0) throw new IllegalArgumentException("Radius cannot be negative");
return Math.PI * radius * radius;
}
What Is a Circle? (Geometric First Principle)
A circle is the set of ALL points in a plane that are exactly distance r
(the radius) from a fixed point (the center). No more, no less.
This definition constrains everything: the shape, the area, the circumference.
It's not "round" — it's precisely defined by one number: r.
Where Does π Come From?
π (pi) is not invented — it's DISCOVERED. It's a ratio that exists in nature:
π = circumference of ANY circle / its diameter
= C / (2r)
= 3.14159265358979323846...
Why is this ratio always the same regardless of circle size? Because all
circles are geometrically similar (same shape, different scale). Scaling
a circle by factor k scales circumference by k and diameter by k.
The ratio C/d = constant for all circles.
π is IRRATIONAL (never terminates, never repeats) and TRANSCENDENTAL
(not a root of any polynomial with integer coefficients).
Proved irrational by Johann Lambert in 1761.
Deriving Area = πr² From Scratch (Calculus Approach)
Imagine cutting a circle into infinitely many thin concentric rings:
Each ring at radius x has:
- circumference = 2πx (the outer edge)
- width         = dx  (infinitesimally thin)
- area          = 2πx · dx

Total area = sum of all ring areas from x=0 to x=r
= ∫₀ʳ 2πx dx
= 2π · [x²/2]₀ʳ
= 2π · (r²/2 - 0)
= πr²   ✓
Deriving Area = πr² Geometrically (No Calculus)
Cut the circle into many thin pizza slices. Rearrange them:
Before:           After rearranging:
●              ___________________
╱╲             |                   |
╱  ╲            |  πr (half circum) |
╱    ╲           |___________________|
╱  ●   ╲               r (radius)

The rearranged shape approaches a RECTANGLE as slices → ∞:
width  = half circumference = πr
height = radius = r
area   = πr × r = πr²  ✓
Why Math.PI * radius * radius Not Math.PI * Math.pow(radius, 2)?
Both give the same result mathematically. But:

radius * radius = ONE multiplication instruction in the CPU
Math.pow(radius, 2) = a function call that handles general cases
(like radius^2.5, radius^-1, etc.), much more computation

For squaring specifically, always use x * x. It's faster and avoids
the floating-point overhead of Math.pow.
Why double Not float?
float  precision: ~7 decimal digits
double precision: ~15 decimal digits

For radius = 5.0:
float:  Area ≈ 78.53981 (7 digits)
double: Area ≈ 78.53981633974483 (15 digits)
For scientific/engineering calculations, float's 7 digits often causes
visibly wrong answers. Always use double for geometry.
The throw Statement: Defensive Programming
javaif (radius < 0) throw new IllegalArgumentException("Radius cannot be negative");
A negative radius is physically meaningless (no circle can have radius -3).
Instead of silently computing π × (-3)² = 28.27 (wrong but looks valid),
we CRASH LOUDLY with a descriptive error.
throw creates an Exception object and unwinds the call stack, stopping
execution at the nearest matching catch block (or crashing the program
if none exists). This is better than silent wrong answers.
IllegalArgumentException specifically means: "the caller passed me
an invalid argument." It's a RuntimeException — no need to declare it.
