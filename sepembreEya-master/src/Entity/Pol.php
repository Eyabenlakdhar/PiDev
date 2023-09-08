<?php

namespace App\Entity;

use App\Repository\PolRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: PolRepository::class)]
class Pol
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\ManyToOne(inversedBy: 'pols')]
    #[ORM\JoinColumn(nullable: false)]
    private ?bob $bob = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getBob(): ?bob
    {
        return $this->bob;
    }

    public function setBob(?bob $bob): static
    {
        $this->bob = $bob;

        return $this;
    }
}
