# Азия Микс

Корпоративный сайт с микро-сервисной архитектурой:
- Первый модуль реализует ведение бухгалтерского учёта с выводом финансовых данных, схем и графиков в удобном и наглядном формате.

Документация по модулю бухгалтерского учёта: `docs/accounting-module.md`.
## Table of Contents

- [Security](#Security)
- [Background](#Background)
- [Install](#Install)
- [Usage](#Usage)
- [API](#API)
- [Contributing](#Contributing)
- [License](#License)
---
## Security

- Аутентификация и авторизация — JWT token собственная реализация.
- Секреты (пароли БД, ключи) хранятся вне репозитория - через переменные окружения `/vault`, не коммитятся в `application.yml`
- Все внешние HTTP-эндпоинты работают только по HTTPS.
---
## Background

Проект «Азия Микс» — корпоративный сайт, построенный как набор независимых микросервисов для последующего масштабирования.

**Первый модуль** отвечает за бухгалтерский учёт: приём и хранение финансовых данных, их агрегация и вывод в наглядном виде — таблицы, диаграммы и схемы, удобные для чтения бухгалтерами и руководством.

**Архитектура:** набор Spring Boot сервисов, взаимодействующих между собой (REST и/или сообщениями через брокер), каждый — со своей зоной ответственности.

---
## Install

This module depends upon a knowledge of [Markdown](https://github.com/RichardLitt/standard-readme/blob/main/example-readmes).

```
npm install asia mix :D
```

Note: The `license` badge image link at the top of this file should be updated with the correct `:user` and `:repo`.
### Any optional sections
---
## Usage
### Any optional sections
---
## API
### Any optional sections
---
## Contributing

- Ветвление: `feature/<название>`, `fix/<название>`.
- PR должны сопровождаться описанием изменений и (при необходимости) обновлением документации в `docs/`.
- Коммиты — в едином стиле
### Any optional sections
---
## License

Лицензия проекта не определена — добавить при необходимости (например, MIT).

[MIT © Richard McRichface.](https://github.com/RichardLitt/standard-readme/blob/main/LICENSE)