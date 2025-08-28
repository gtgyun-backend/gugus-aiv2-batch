# Spring Boot에서의 도메인 주도 설계(DDD) 패턴

## DDD(Domain-Driven Design)란?
도메인 주도 설계는 소프트웨어의 복잡성을 다루기 위한 접근 방식으로, 비즈니스 도메인을 중심으로 설계하는 방법론입니다.

## DDD의 주요 구성요소

### 1. 계층형 아키텍처
- Presentation Layer: 사용자 인터페이스 (Controller)
- Application Layer: 비즈니스 로직 조정 (Service)
- Domain Layer: 핵심 비즈니스 로직 (Domain Model)
- Infrastructure Layer: 데이터 저장소, 외부 시스템 연동 (Repository)

### 2. 도메인 모델의 구성요소
- Entity: 고유한 식별자를 가지는 객체 (예: @Entity 클래스)
- Value Object: 식별자가 없는 불변 객체
- Aggregate: 연관된 Entity와 Value Object의 집합
- Repository: 도메인 객체의 저장소
- Domain Service: 특정 Entity에 속하지 않는 도메인 로직

계층 분리
애플리케이션 서비스: 트랜잭션 관리
도메인 서비스: 비즈니스 규칙
도메인 모델: 상태와 행위

``` java
@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalDomainService domainService;
    private final RentalRepository rentalRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public IRentalById createNewRental(Items items, Users users, IRentalRequests request) {

        // 2. 도메인 검증
        domainService.validateRentalCreation(items, period);

        // 3. Aggregate 생성
        RentalCreationContext context = RentalCreationContext.from(
            items, users, request, period,
            domainService.getRequiredRentalInfo(items.getItemNo())
        );
        
        RentalAggregate rentalAggregate = RentalAggregate.create(context);

        // 4. 저장
        Rentals savedRental = rentalRepository.save(rentalAggregate.getRental());
        
        // 5. 도메인 이벤트 발행
        eventPublisher.publishEvent(new RentalCreatedEvent(savedRental));

        return rentalMapper.toDto(savedRental);
    }
}
```

1. 관심사의 분리  
도메인 모델: 비즈니스 규칙과 상태 관리  
서비스 계층: 트랜잭션 관리, 외부 시스템 연동  
2. 도메인 모델의 순수성  
프레임워크 독립적   
테스트 용이성  
재사용 가능성  
3. 유지보수성  
도메인 로직 변경이 용이  
기술 스택 변경에 영향 받지 않음
4. 테스트 용이성  
단위 테스트 작성 용이  
목킹(mocking) 불필요  