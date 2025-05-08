package com.nuomi.backend.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuomi.backend.mapper.ProductImgMapper;
import com.nuomi.backend.mapper.ProductMapper;
import com.nuomi.backend.model.Product;
import com.nuomi.backend.model.ProductImg;
import com.nuomi.backend.model.VO.ProductDetailVO;
import com.nuomi.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Service
public class ProductServiceImp extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductImgMapper productImgMapper;
    @Override
    public List<ProductDetailVO> ProductDetailVOList(Long lastId, Integer pageSize) {
        return productMapper.selectProductFeed(lastId, pageSize);
    }
    @Override
    public boolean saveProduct(String productName, String productTitle, String productDescription,
                               String productPrice, List<String> tags, MultipartFile mainImage,
                               List<MultipartFile> otherImages) {


        return true; // return success or failure based on the save operation
    }
    private static final String UPLOAD_DIR = "D:\\毕业设计\\李文栋\\frontend2.0\\public\\uploads\\"; // 以你前端项目 public 文件夹为准

    private String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("上传图片不能为空");
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) throw new IllegalArgumentException("图片名不能为空");
        String filename = IdWorker.getId() + "_" + originalFilename;
        File dest = new File(UPLOAD_DIR + filename);
        dest.getParentFile().mkdirs();
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("图片保存失败: " + e.getMessage());
        }
        return "/uploads/" + filename;
    }

    private String joinTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) return null;
        StringJoiner joiner = new StringJoiner(",");
        tags.forEach(joiner::add);
        return joiner.toString();
    }

    @Override
    public Product createProduct(
            String productName,
            String productTitle,
            String productDescription,
            BigDecimal productPrice,
            List<String> tags,
            MultipartFile mainImage,
            List<MultipartFile> otherImages,
            Integer categoryId,
            Long createUserId
    ) {
        // 参数校验
        if (productName == null || productName.trim().isEmpty()) throw new IllegalArgumentException("商品名称不能为空");
        if (productTitle == null || productTitle.trim().isEmpty()) throw new IllegalArgumentException("商品标题不能为空");
        if (productDescription == null || productDescription.trim().isEmpty()) throw new IllegalArgumentException("商品描述不能为空");
        if (productPrice == null || productPrice.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("商品价格必须大于0");
        if (mainImage == null || mainImage.isEmpty()) throw new IllegalArgumentException("主图不能为空");
        if (categoryId == null) throw new IllegalArgumentException("商品分类不能为空");
        if (createUserId == null) throw new IllegalArgumentException("创建用户不能为空");

        Product product = new Product();
        product.setName(productName);
        product.setTitle(productTitle);
        product.setPrice(productPrice);
        product.setTags(joinTags(tags));
        product.setMainImageUrl(saveImage(mainImage));
        product.setImageUrl(null);
        product.setCreateUserId(createUserId);
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        product.setCategoryId(categoryId);
        product.setProductDescription(productDescription);

        try {
            productMapper.insert(product);
        } catch (Exception e) {
            throw new RuntimeException("商品信息插入数据库失败: " + e.getMessage());
        }

        // 保存其他图片
        if (otherImages != null && !otherImages.isEmpty()) {
            for (MultipartFile file : otherImages) {
                if (file == null || file.isEmpty()) continue;
                String url = saveImage(file);
                ProductImg img = new ProductImg();
                img.setProductId(product.getProductId());
                img.setImgUrl(url);
                try {
                    productImgMapper.insert(img);
                } catch (Exception e) {
                    // 不影响主流程，但可记录日志
                    System.err.println("商品图片保存失败: " + e.getMessage());
                }
            }
        }
        return product;
    }

    @Override
    public Product selectById(Long id) {
        System.out.println("id: " + id);
        Product product = productMapper.selectById(id);
        System.out.println("product: " + product);
//        if (product != null) {
//            // 获取商品附加图片（如有）
//            List<ProductImg> imgList = productImgMapper.selectList(
//                    new LambdaQueryWrapper<ProductImg>().eq(ProductImg::getProductId, id)
//            );
//            // 可将图片URL列表设置到Product里，便于前端展示
//            product.setImgUrls(
//                    imgList.stream().map(ProductImg::getImgUrl).toList()
//            );
//        }
        return product;
    }

    @Override
    public List<String> selectProductImgById(Long productId) {
        return productImgMapper.selectList(
                new LambdaQueryWrapper<ProductImg>().eq(ProductImg::getProductId, productId)
        ).stream().map(ProductImg::getImgUrl).toList();
    }
    // 新增：模糊搜索
    @Override
    public List<ProductDetailVO> searchProductList(String keyword, Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return productMapper.searchProductList("%" + keyword + "%", pageSize, offset);
    }
}


