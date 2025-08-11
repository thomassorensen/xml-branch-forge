# Backend: lovabletestapp (Spring Boot)

CCMS backend with filesystem XML storage and MySQL metadata. Supports versioning tags like a1, a2, and branching like a1a1, a1a2.

Quick start
- With Docker (recommended)
  - cd backend
  - docker compose up --build
  - API at http://localhost:8080, Swagger UI at http://localhost:8080/swagger-ui.html

- Local dev (requires Java 21, Maven, MySQL)
  - Set env: DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD, FILE_STORAGE_PATH
  - mvn spring-boot:run

Versioning rules
- First version under an item: a1, then a2, a3...
- Branch from a version V by supplying parentTag=V when uploading; children become Va1, Va2...

Key endpoints
- POST /api/items {title, keySlug}
- GET /api/items
- POST /api/items/{itemId}/versions (multipart file=XML, optional parentTag)
- GET /api/items/{itemId}/versions
- GET /api/items/{itemId}/versions/{versionTag}/file

Notes
- Files written under FILE_STORAGE_PATH/{itemId}/{versionTag}.xml (Windows/Linux compatible)
- Schema auto-managed (spring.jpa.hibernate.ddl-auto=update)
