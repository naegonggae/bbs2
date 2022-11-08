package com.mustache.bbs2.controller;

import com.mustache.bbs2.domain.Article;
import com.mustache.bbs2.domain.dto.ArticleDto;
import com.mustache.bbs2.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/articles")
@Slf4j
public class ArticleController {
    // Spring이 ArticleRepository구현체를 DI (인터페이스 아님) Dao를 알아서 만들어줘서 사용함
    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "list";
    }

    @GetMapping("")
    public String index() {
        return "redirect:/articles/list";
    }

    @GetMapping("/new")
    public String createPage() {
        return "new";
    }

    @GetMapping("/{id}")
    public String selectSingle(@PathVariable Long id, Model model) {
        Optional<Article> optArticle = articleRepository.findById(id);

        if (!optArticle.isEmpty()) {
            // Optional.get() ---> Article
            model.addAttribute("article", optArticle.get());
            return "show";
        } else {
            return "error";
        }
    }

    @PostMapping("")
    public String articles(ArticleDto articleDto) {
        log.info(articleDto.getTitle());
        Article savedArticle = articleRepository.save(articleDto.toEntity());
        log.info("generatedId:{}", savedArticle.getId());
        return String.format("redirect:/articles/%d", savedArticle.getId());
    }
}
