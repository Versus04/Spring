# Hiring Client

Spring Boot client that auto-submits a SQL query answer.

## How to use
1. `java -jar target/hiring-client.jar`
2. It will:
   - Generate webhook with your details
   - Pick correct SQL query
   - Submit the query automatically

## Final SQL Query

```sql
SELECT e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME,
       COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT
FROM EMPLOYEE e
JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
LEFT JOIN EMPLOYEE e2
       ON e.DEPARTMENT = e2.DEPARTMENT
      AND e2.DOB > e.DOB
GROUP BY e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME
ORDER BY e.EMP_ID DESC;
```
