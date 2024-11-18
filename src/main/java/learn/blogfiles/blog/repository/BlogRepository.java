package learn.blogfiles.blog.repository;


import learn.blogfiles.blog.model.BlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, String> {


    @Query("SELECT b FROM BlogEntity b WHERE b.name = :name")
    BlogEntity byName(@Param("name") String name);
}
