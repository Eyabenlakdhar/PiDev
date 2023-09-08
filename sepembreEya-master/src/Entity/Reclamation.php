<?php

namespace App\Entity;

use App\Repository\ReclamationRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: ReclamationRepository::class)]
class Reclamation
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $recText = null;

    #[ORM\Column(type: Types::DATE_MUTABLE, nullable: true)]
    private ?\DateTimeInterface $date_Rec = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $recSubject = null;

    #[ORM\ManyToOne(inversedBy: 'reclamations')]
    private ?User $id_user = null;

    #[ORM\Column]
    private ?bool $etatReservation = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $reponse = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getRecText(): ?string
    {
        return $this->recText;
    }

    public function setRecText(?string $recText): static
    {
        $this->recText = $recText;

        return $this;
    }

    public function getDateRec(): ?\DateTimeInterface
    {
        return $this->date_Rec;
    }

    public function setDateRec(?\DateTimeInterface $date_Rec): static
    {
        $this->date_Rec = $date_Rec;

        return $this;
    }

    public function getRecSubject(): ?string
    {
        return $this->recSubject;
    }

    public function setRecSubject(?string $recSubject): static
    {
        $this->recSubject = $recSubject;

        return $this;
    }

    public function getIdUser(): ?User
    {
        return $this->id_user;
    }

    public function setIdUser(?User $id_user): static
    {
        $this->id_user = $id_user;

        return $this;
    }

    public function isEtatReservation(): ?bool
    {
        return $this->etatReservation;
    }

    public function setEtatReservation(bool $etatReservation): static
    {
        $this->etatReservation = $etatReservation;

        return $this;
    }

    public function getReponse(): ?string
    {
        return $this->reponse;
    }

    public function setReponse(?string $reponse): static
    {
        $this->reponse = $reponse;

        return $this;
    }
}
