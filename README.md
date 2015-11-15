# tr39-confusables [![Build Status](https://travis-ci.org/mpkorstanje/tr39-confusables.svg)](https://travis-ci.org/mpkorstanje/tr39-confusables)
Skeleton algorithm from [Unicode TR39](http://www.unicode.org/reports/tr39/) for testing confusability of strings.

## Usage ##
```
import static com.github.mpkorstanje.unicode.tr39confusables.Skeleton.skeleton;
...
// Skeleton representations of unicode strings containing 
// confusable characters are equal 
skeleton("paypal").equals(skeleton("paypal")); // true
skeleton("paypal").equals(skeleton("𝔭𝒶ỿ𝕡𝕒ℓ")); // true
skeleton("paypal").equals(skeleton("ρ⍺у𝓅𝒂ן")); // true
skeleton("ρ⍺у𝓅𝒂ן").equals(skeleton("𝔭𝒶ỿ𝕡𝕒ℓ")); // true
skeleton("ρ⍺у𝓅𝒂ן").equals(skeleton("𝔭𝒶ỿ𝕡𝕒ℓ")); // true

// The skeleton representation does not transform case
skeleton("payPal").equals(skeleton("paypal")); // false

// The skeleton representation does not remove diacritics
skeleton("paypal").equals(skeleton("pàỳpąl")); // false
```
Note on the use of `Skeleton`, from TR39:

>  A skeleton is intended only for internal use for testing confusability of strings; the resulting text is not suitable for display to users, because it will appear to be a hodgepodge of different scripts. In particular, the result of mapping an identifier will not necessary be an identifier. Thus the confusability mappings can be used to test whether two identifiers are confusable (if their skeletons are the same), but should definitely not be used as a "normalization" of identifiers. 


## Maven ##

```
<dependency>
  <groupId>com.github.mpkorstanje</groupId>
  <artifactId>tr39-confusables-skeleton</artifactId>
  <version>0.5.0</version>
</dependency>
```
