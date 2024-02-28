package pl.com.coders.shop2.domain;

public enum CategoryType {
    ELEKTRONIKA(1L),
    MOTORYZACJA(2L),
    EDUKACJA(3L),
    INNE(4L);

    private Long id;

    CategoryType(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    }
