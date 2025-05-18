# Stage 1: build
FROM openjdk:17-slim AS builder

# Set working dir
WORKDIR /app

# Copy all source files
COPY . .

# Compile all .java files into .class files
# This assumes your .java files declare their packages (exception, model, service, ui)
RUN find . -name "*.java" > sources.txt \
 && javac @sources.txt

# Stage 2: runtime
FROM openjdk:17-slim

WORKDIR /app

# Copy compiled classes from builder
COPY --from=builder /app .

# Default command: run your Main class
CMD ["java", "ui.Main"]
