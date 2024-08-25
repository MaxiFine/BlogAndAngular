package learn.blogfiles.blog.repository;


import learn.blogfiles.blog.model.BlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, String> {

}
