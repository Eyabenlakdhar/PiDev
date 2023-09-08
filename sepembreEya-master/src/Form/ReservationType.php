<?php

namespace App\Form;

use App\Entity\Reservation;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType; // Import the TextareaType
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\TextType;


class ReservationType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
          
            ->add('type', ChoiceType::class, [
                'label' => 'Select your Service',
                'choices'  => [
                    'mecanicien' => 'mecanicien',
                    'femme de ménage' => 'femme de menage ',
                    'plombier' => 'plombier',
                    'medecin' => 'medecin',
                    

                ], 'attr' => ['class' => 'message-box form-control', 'placeholder' => 'Write your reclamation',
                
            ],]
            )       
            ->add('date_res', DateType::class, [
                // renders it as a single text box
                'label' => 'Reservation Date ',
                'widget' => 'single_text',
                
                'attr' => ['class' => 'message-box form-control', 'placeholder' => 'Write your reclamation',
                
            ],])    
            ->add('comment', TextareaType::class, [
                'label' => 'Comment',
               
                'attr' => ['class' => 'message-box form-control', 'placeholder' => 'any comment you want to add ?'],
            ])
            ->add('price' , TextType::class, [
                'label' => 'Price Offer',
                'attr' => ['class' => 'form-control', 'placeholder' => 'Price $/hour'],
            ])
            ->add('id_user')
         ;
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Reservation::class,
        ]);
    }
}
