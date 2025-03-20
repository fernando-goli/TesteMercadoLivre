# Mercado Libre Mobile Candidate

## Features
 - Aplicativo android desenvolvido usando a [API](https://api.mercadolibre.com/) do mercado livre.
 - Pesquise de produtos na API e exiba-os em uma lista.
 - Ver detalhes de um produto.

### Este aplicativo foi desenvolvido usando Kotlin e utiliza os seguintes componentes:

- Jetpack components
- Coroutines
- Clean architecture (Domain, Data, Presentation)
- MVVM
- Repository pattern
- Use cases
- Livedata
- Mutable State
- Timber (Logs)
- Dagger Hilt (Dependency injection)
- Glide (Load images)
- Retrofit (HTTP requests)
- Pagination

## Arquitetura
O aplicativo é construído usando o padrão Clean Architecture baseado em [Architecture Components](https://developer.android.com/jetpack/guide#recommended-app-arch) no Android. O aplicativo é dividido em três camadas:

- Domínio: Esta camada contém a lógica de negócios da aplicação, aqui definimos os modelos de dados e os casos de uso.
- Dados: Esta camada contém a camada de dados da aplicação. Ela contém o banco de dados, a rede e a implementação do repositório.
- Apresentação: Esta camada contém a camada de apresentação do aplicativo.

Search Screen | Overview Screen | Details Screen
--- | --- | ---
<img src="screenshot_search_screen.pgn" width="300" alt="Search Screen"/> | <img src="screenshot_list_products.pgn" width="300" alt="Overview Screen"/> | <img src="screenshot_detail_products.png" width="300" alt="Details Screen"/>
