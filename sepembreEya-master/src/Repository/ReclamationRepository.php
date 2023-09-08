<?php

namespace App\Repository;

use App\Entity\Reclamation;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;
use Doctrine\ORM\QueryBuilder;


/**
 * @extends ServiceEntityRepository<Reclamation>
 *
 * @method Reclamation|null find($id, $lockMode = null, $lockVersion = null)
 * @method Reclamation|null findOneBy(array $criteria, array $orderBy = null)
 * @method Reclamation[]    findAll()
 * @method Reclamation[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class ReclamationRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Reclamation::class);
    }

    public function calculateStatistics()
{
    $qb = $this->createQueryBuilder('r');
    $qb->select('COUNT(r.id) as totalReclamations')
       //->addSelect('AVG(r.type) as averageType')
       ->addSelect('MAX(r.date_Rec) as latestReclamationDate');

    $result = $qb->getQuery()->getSingleResult();

    return $result;
}

public function searchDql($query)
    {
        $qb = $this->createQueryBuilder('r');

        // Create a WHERE clause that matches the recText field against the search query
        $whereClause = $qb->expr()->like('r.recText', ':query');

        // Add the WHERE clause to the query builder
        $qb->andWhere($whereClause)
           ->setParameter('query', '%' . $query . '%');

        // Execute the query and return the results
        return $qb->getQuery()->getResult();
    }


    public function findAllAscending(string $criteria): array
    {
        return $this->createQueryBuilder('r')
            ->orderBy($criteria, 'ASC') // Replace 'fieldToSortBy' with the actual field name you want to sort by
            ->getQuery()
            ->getResult();
    }

    public function findAllDescending(string $criteria): array
    {
        return $this->createQueryBuilder('r')
            ->orderBy($criteria, 'DESC') // Replace 'fieldToSortBy' with the actual field name you want to sort by
            ->getQuery()
            ->getResult();
    }

//    /**
//     * @return Reclamation[] Returns an array of Reclamation objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('r')
//            ->andWhere('r.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('r.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?Reclamation
//    {
//        return $this->createQueryBuilder('r')
//            ->andWhere('r.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
